package view;

import controller.ReportsController;
import model.SalesReport;
import model.ProductSalesReport;
import model.InventoryReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Reports dashboard view for admin operations
 */
public class ReportsDashboardView extends JFrame {
    
    private ReportsController reportsController;
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JLabel totalSalesLabel, totalOrdersLabel, totalCustomersLabel, inventoryValueLabel;
    private JTable salesTable, topProductsTable, inventoryTable, lowStockTable;
    private DefaultTableModel salesTableModel, topProductsTableModel, inventoryTableModel, lowStockTableModel;
    private JSpinner lowStockThresholdSpinner;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color SUCCESS_COLOR = new Color(34, 139, 34);
    private static final Color WARNING_COLOR = new Color(255, 140, 0);
    private static final Color DANGER_COLOR = new Color(220, 20, 60);
    
    public ReportsDashboardView() {
        this.reportsController = new ReportsController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadDashboardData();
    }
    
    private void initializeComponents() {
        setTitle("Reports Dashboard - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Initialize tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        
        // Initialize summary labels
        totalSalesLabel = createSummaryLabel("Total Sales: LKR 0.00", SUCCESS_COLOR);
        totalOrdersLabel = createSummaryLabel("Total Orders: 0", PRIMARY_COLOR);
        totalCustomersLabel = createSummaryLabel("Total Customers: 0", SECONDARY_COLOR);
        inventoryValueLabel = createSummaryLabel("Inventory Value: LKR 0.00", WARNING_COLOR);
        
        // Initialize tables
        initializeTables();
        
        // Initialize other components
        lowStockThresholdSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    }
    
    private JLabel createSummaryLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(color);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }
    
    private void initializeTables() {
        // Sales table
        String[] salesColumns = {"Date", "Total Sales (LKR)", "Orders", "Avg Order Value"};
        salesTableModel = new DefaultTableModel(salesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesTableModel);
        styleTable(salesTable);
        
        // Top products table
        String[] productColumns = {"Product", "Quantity Sold", "Revenue (LKR)", "Avg Price"};
        topProductsTableModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        topProductsTable = new JTable(topProductsTableModel);
        styleTable(topProductsTable);
        
        // Inventory table
        String[] inventoryColumns = {"Product", "Current Stock", "Value (LKR)", "Category"};
        inventoryTableModel = new DefaultTableModel(inventoryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(inventoryTableModel);
        styleTable(inventoryTable);
        
        // Low stock table
        String[] lowStockColumns = {"Product", "Current Stock", "Threshold", "Status"};
        lowStockTableModel = new DefaultTableModel(lowStockColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lowStockTable = new JTable(lowStockTableModel);
        styleTable(lowStockTable);
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(25);
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Reports & Analytics Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Summary panel
        JPanel summaryPanel = createSummaryPanel();
        
        // Create tabs
        createSalesReportTab();
        createProductAnalyticsTab();
        createInventoryReportTab();
        createLowStockTab();
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(summaryPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton refreshButton = new JButton("Refresh Reports");
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadDashboardData());
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        summaryPanel.setBackground(BACKGROUND_COLOR);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        summaryPanel.add(totalSalesLabel);
        summaryPanel.add(totalOrdersLabel);
        summaryPanel.add(totalCustomersLabel);
        summaryPanel.add(inventoryValueLabel);
        
        return summaryPanel;
    }
    
    private void createSalesReportTab() {
        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanel.setBackground(BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daily Sales Report"));
        salesPanel.add(scrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("Sales Report", salesPanel);
    }
    
    private void createProductAnalyticsTab() {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(topProductsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Top Selling Products"));
        productPanel.add(scrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("Product Analytics", productPanel);
    }
    
    private void createInventoryReportTab() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(BACKGROUND_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Current Inventory"));
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("Inventory Report", inventoryPanel);
    }
    
    private void createLowStockTab() {
        JPanel lowStockPanel = new JPanel(new BorderLayout());
        lowStockPanel.setBackground(BACKGROUND_COLOR);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Low Stock Threshold:"));
        controlPanel.add(lowStockThresholdSpinner);
        
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> loadLowStockData());
        controlPanel.add(updateButton);
        
        lowStockPanel.add(controlPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(lowStockTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Low Stock Alert"));
        lowStockPanel.add(scrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("Low Stock Alert", lowStockPanel);
    }
    
    private void setupEventHandlers() {
        // Event handlers are set up in the create methods above
    }
    
    private void loadDashboardData() {
        loadSummaryData();
        loadSalesData();
        loadTopProductsData();
        loadInventoryData();
        loadLowStockData();
    }
    
    private void loadSummaryData() {
        try {
            // Get current month's data
            Date startDate = Date.valueOf(LocalDate.now().withDayOfMonth(1));
            Date endDate = Date.valueOf(LocalDate.now());
            
            SalesReport monthlyReport = reportsController.getSalesReportByDateRange(startDate, endDate);
            
            totalSalesLabel.setText("Total Sales: LKR " + String.format("%.2f", monthlyReport.getTotalSales()));
            totalOrdersLabel.setText("Total Orders: " + monthlyReport.getTotalOrders());
            
            // Mock data for customers and inventory value
            totalCustomersLabel.setText("Total Customers: 150");
            inventoryValueLabel.setText("Inventory Value: LKR 125,000.00");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadSalesData() {
        try {
            salesTableModel.setRowCount(0);
            
            // Load last 7 days of sales data
            for (int i = 6; i >= 0; i--) {
                Date date = Date.valueOf(LocalDate.now().minusDays(i));
                SalesReport report = reportsController.getDailySalesReport(date);
                
                Object[] row = {
                    date.toString(),
                    String.format("%.2f", report.getTotalSales()),
                    report.getTotalOrders(),
                    String.format("%.2f", report.getAverageOrderValue())
                };
                salesTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTopProductsData() {
        try {
            topProductsTableModel.setRowCount(0);
            
            List<ProductSalesReport> topProducts = reportsController.getTopSellingProducts(10);
            
            for (ProductSalesReport product : topProducts) {
                Object[] row = {
                    product.getProductName(),
                    product.getQuantitySold(),
                    String.format("%.2f", product.getTotalRevenue()),
                    String.format("%.2f", product.getAveragePrice())
                };
                topProductsTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadInventoryData() {
        try {
            inventoryTableModel.setRowCount(0);
            
            List<InventoryReport> inventory = reportsController.getInventoryReport();
            
            for (InventoryReport item : inventory) {
                Object[] row = {
                    item.getProductName(),
                    item.getCurrentStock(),
                    String.format("%.2f", item.getInventoryValue()),
                    item.getCategoryName()
                };
                inventoryTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadLowStockData() {
        try {
            lowStockTableModel.setRowCount(0);
            
            int threshold = (Integer) lowStockThresholdSpinner.getValue();
            List<InventoryReport> lowStockItems = reportsController.getLowStockItems(threshold);
            
            for (InventoryReport item : lowStockItems) {
                String status = item.getCurrentStock() == 0 ? "OUT OF STOCK" : "LOW STOCK";
                Object[] row = {
                    item.getProductName(),
                    item.getCurrentStock(),
                    threshold,
                    status
                };
                lowStockTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
