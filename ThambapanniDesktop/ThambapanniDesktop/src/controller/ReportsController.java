package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import model.SalesReport;
import model.ProductSalesReport;
import model.InventoryReport;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import database.DatabaseConnection;

/**
 * Controller for generating various reports
 */
public class ReportsController {
    
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    
    public ReportsController() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Generate daily sales report
     * @param date Specific date
     * @return SalesReport for the date
     */
    public SalesReport getDailySalesReport(Date date) {
        String sql = """
            SELECT 
                COUNT(o.order_id) as total_orders,
                COALESCE(SUM(o.total_amount), 0) as total_sales,
                COUNT(DISTINCT o.user_id) as total_customers
            FROM orders o 
            WHERE DATE(o.created_at) = ? 
            AND o.order_status != 'CANCELLED'
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, date);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SalesReport report = new SalesReport();
                    report.setReportDate(date);
                    report.setTotalOrders(rs.getInt("total_orders"));
                    report.setTotalSales(rs.getBigDecimal("total_sales"));
                    report.setTotalCustomers(rs.getInt("total_customers"));
                    report.setPeriod("DAILY");
                    return report;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new SalesReport(date, 0, BigDecimal.ZERO, "DAILY");
    }
    
    /**
     * Generate monthly sales report
     * @param year Year
     * @param month Month (1-12)
     * @return SalesReport for the month
     */
    public SalesReport getMonthlySalesReport(int year, int month) {
        String sql = """
            SELECT 
                COUNT(o.order_id) as total_orders,
                COALESCE(SUM(o.total_amount), 0) as total_sales,
                COUNT(DISTINCT o.user_id) as total_customers
            FROM orders o 
            WHERE YEAR(o.created_at) = ? AND MONTH(o.created_at) = ?
            AND o.order_status != 'CANCELLED'
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SalesReport report = new SalesReport();
                    report.setReportDate(Date.valueOf(year + "-" + String.format("%02d", month) + "-01"));
                    report.setTotalOrders(rs.getInt("total_orders"));
                    report.setTotalSales(rs.getBigDecimal("total_sales"));
                    report.setTotalCustomers(rs.getInt("total_customers"));
                    report.setPeriod("MONTHLY");
                    return report;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new SalesReport();
    }
    
    /**
     * Get top selling products
     * @param limit Number of products to return
     * @return List of ProductSalesReport
     */
    public List<ProductSalesReport> getTopSellingProducts(int limit) {
        List<ProductSalesReport> reports = new ArrayList<>();
        String sql = """
            SELECT 
                p.product_id,
                p.product_name,
                c.category_name,
                SUM(oi.quantity) as quantity_sold,
                SUM(oi.quantity * oi.unit_price) as total_revenue,
                AVG(oi.unit_price) as average_price,
                COUNT(DISTINCT oi.order_id) as total_orders
            FROM products p
            JOIN order_items oi ON p.product_id = oi.product_id
            JOIN orders o ON oi.order_id = o.order_id
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE o.order_status != 'CANCELLED'
            GROUP BY p.product_id, p.product_name, c.category_name
            ORDER BY quantity_sold DESC
            LIMIT ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ProductSalesReport report = new ProductSalesReport();
                    report.setProductId(rs.getInt("product_id"));
                    report.setProductName(rs.getString("product_name"));
                    report.setCategoryName(rs.getString("category_name"));
                    report.setQuantitySold(rs.getInt("quantity_sold"));
                    report.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                    report.setAveragePrice(rs.getBigDecimal("average_price"));
                    report.setTotalOrders(rs.getInt("total_orders"));
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    /**
     * Get inventory report
     * @return List of InventoryReport
     */
    public List<InventoryReport> getInventoryReport() {
        List<InventoryReport> reports = new ArrayList<>();
        String sql = """
            SELECT 
                p.product_id,
                p.product_name,
                c.category_name,
                p.stock_quantity,
                p.price
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            ORDER BY p.stock_quantity ASC, p.product_name
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                InventoryReport report = new InventoryReport();
                report.setProductId(rs.getInt("product_id"));
                report.setProductName(rs.getString("product_name"));
                report.setCategoryName(rs.getString("category_name"));
                report.setCurrentStock(rs.getInt("stock_quantity"));
                report.setUnitPrice(rs.getBigDecimal("price"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    /**
     * Get low stock products
     * @param threshold Stock threshold
     * @return List of InventoryReport for low stock items
     */
    public List<InventoryReport> getLowStockReport(int threshold) {
        List<InventoryReport> reports = new ArrayList<>();
        String sql = """
            SELECT 
                p.product_id,
                p.product_name,
                c.category_name,
                p.stock_quantity,
                p.price
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.category_id
            WHERE p.stock_quantity <= ?
            ORDER BY p.stock_quantity ASC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threshold);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InventoryReport report = new InventoryReport();
                    report.setProductId(rs.getInt("product_id"));
                    report.setProductName(rs.getString("product_name"));
                    report.setCategoryName(rs.getString("category_name"));
                    report.setCurrentStock(rs.getInt("stock_quantity"));
                    report.setUnitPrice(rs.getBigDecimal("price"));
                    report.setReorderLevel(threshold);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    /**
     * Get sales summary for date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of daily sales reports
     */
    public List<SalesReport> getSalesReportByDateRange(Date startDate, Date endDate) {
        List<SalesReport> reports = new ArrayList<>();
        String sql = """
            SELECT 
                DATE(o.created_at) as report_date,
                COUNT(o.order_id) as total_orders,
                COALESCE(SUM(o.total_amount), 0) as total_sales,
                COUNT(DISTINCT o.user_id) as total_customers
            FROM orders o 
            WHERE DATE(o.created_at) BETWEEN ? AND ?
            AND o.order_status != 'CANCELLED'
            GROUP BY DATE(o.created_at)
            ORDER BY report_date DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SalesReport report = new SalesReport();
                    report.setReportDate(rs.getDate("report_date"));
                    report.setTotalOrders(rs.getInt("total_orders"));
                    report.setTotalSales(rs.getBigDecimal("total_sales"));
                    report.setTotalCustomers(rs.getInt("total_customers"));
                    report.setPeriod("DAILY");
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reports;
    }
    
    /**
     * Get customer statistics
     * @return Array with [totalCustomers, newCustomersThisMonth, activeCustomers]
     */
    public Object[] getCustomerStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_customers,
                SUM(CASE WHEN MONTH(created_at) = MONTH(CURRENT_DATE) 
                    AND YEAR(created_at) = YEAR(CURRENT_DATE) THEN 1 ELSE 0 END) as new_customers_this_month,
                COUNT(DISTINCT CASE WHEN EXISTS (
                    SELECT 1 FROM orders o WHERE o.user_id = u.user_id 
                    AND o.created_at >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
                ) THEN u.user_id END) as active_customers
            FROM users u 
            WHERE u.user_type = 'CUSTOMER'
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("total_customers"),
                    rs.getInt("new_customers_this_month"),
                    rs.getInt("active_customers")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new Object[]{0, 0, 0};
    }
    
    /**
     * Get total inventory value
     * @return Total inventory value
     */
    public BigDecimal getTotalInventoryValue() {
        String sql = "SELECT SUM(stock_quantity * price) as total_value FROM products";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total_value");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return BigDecimal.ZERO;
    }
}
