package dao;

import database.DatabaseConnection;
import model.Order;
import model.Order.OrderStatus;
import model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order operations
 */
public class OrderDAO {
    
    /**
     * Create a new order
     * @param order Order object to create
     * @return true if successful, false otherwise
     */
    public boolean createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setBigDecimal(2, order.getTotalAmount());
            pstmt.setString(3, order.getOrderStatus().name());
            pstmt.setString(4, order.getShippingAddress());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get order by ID
     * @param orderId Order ID
     * @return Order object if found, null otherwise
     */
    public Order getOrderById(int orderId) {
        String sql = """
            SELECT o.*, u.full_name as customer_name, u.email as customer_email 
            FROM orders o 
            LEFT JOIN users u ON o.user_id = u.user_id 
            WHERE o.order_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all orders
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.*, u.full_name as customer_name, u.email as customer_email 
            FROM orders o 
            LEFT JOIN users u ON o.user_id = u.user_id 
            ORDER BY o.created_at DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Get orders by user ID
     * @param userId User ID
     * @return List of orders for the user
     */
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.*, u.full_name as customer_name, u.email as customer_email 
            FROM orders o 
            LEFT JOIN users u ON o.user_id = u.user_id 
            WHERE o.user_id = ? 
            ORDER BY o.created_at DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Get orders by status
     * @param status Order status
     * @return List of orders with the specified status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.*, u.full_name as customer_name, u.email as customer_email 
            FROM orders o 
            LEFT JOIN users u ON o.user_id = u.user_id 
            WHERE o.order_status = ? 
            ORDER BY o.created_at DESC
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    /**
     * Update order status
     * @param orderId Order ID
     * @param status New order status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            pstmt.setInt(2, orderId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update order information
     * @param order Order object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET total_amount = ?, order_status = ?, shipping_address = ? WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, order.getTotalAmount());
            pstmt.setString(2, order.getOrderStatus().name());
            pstmt.setString(3, order.getShippingAddress());
            pstmt.setInt(4, order.getOrderId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete order by ID
     * @param orderId Order ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get total sales amount
     * @return Total sales amount
     */
    public BigDecimal getTotalSales() {
        String sql = "SELECT SUM(total_amount) FROM orders WHERE order_status != 'CANCELLED'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Get order count by status
     * @param status Order status
     * @return Number of orders with the specified status
     */
    public int getOrderCountByStatus(OrderStatus status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE order_status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.name());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    /**
     * Get order statistics
     * @return Array with [totalOrders, pendingOrders, completedOrders, totalRevenue]
     */
    public Object[] getOrderStatistics() {
        String sql = """
            SELECT
                COUNT(*) as total_orders,
                SUM(CASE WHEN order_status = 'PENDING' THEN 1 ELSE 0 END) as pending_orders,
                SUM(CASE WHEN order_status = 'DELIVERED' THEN 1 ELSE 0 END) as completed_orders,
                SUM(CASE WHEN order_status != 'CANCELLED' THEN total_amount ELSE 0 END) as total_revenue
            FROM orders
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Object[]{
                    rs.getInt("total_orders"),
                    rs.getInt("pending_orders"),
                    rs.getInt("completed_orders"),
                    rs.getBigDecimal("total_revenue") != null ? rs.getBigDecimal("total_revenue") : BigDecimal.ZERO
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[]{0, 0, 0, BigDecimal.ZERO};
    }

    /**
     * Map ResultSet to Order object
     * @param rs ResultSet
     * @return Order object
     * @throws SQLException
     */
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerEmail(rs.getString("customer_email"));
        return order;
    }
}
