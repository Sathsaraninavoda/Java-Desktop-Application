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
    private JDateChooser startDateChooser, endDateChooser;
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
        totalSalesLabel = createSummaryLabel("Total Sales: LKR 0.00");
        totalOrdersLabel = createSummaryLabel("Total Orders: 0");
        totalCustomersLabel = createSummaryLabel("Total Customers: 0");
        inventoryValueLabel = createSummaryLabel("Inventory Value: LKR 0.00");
        
        // Initialize date choosers
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        startDateChooser.setDate(java.sql.Date.valueOf(LocalDate.now().minusDays(30)));
        endDateChooser.setDate(java.sql.Date.valueOf(LocalDate.now()));
        
        // Initialize spinner
        lowStockThresholdSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        
        // Initialize tables
        initializeTables();
    }
    
    private void initializeTables() {
        // Sales table
        String[] salesColumns = {"Date", "Orders", "Sales (LKR)", "Avg Order Value (LKR)", "Customers"};
        salesTableModel = new DefaultTableModel(salesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        salesTable = new JTable(salesTableModel);
        styleTable(salesTable);
        
        // Top products table
        String[] productsColumns = {"Product", "Category", "Qty Sold", "Revenue (LKR)", "Avg Price (LKR)", "Orders"};
        topProductsTableModel = new DefaultTableModel(productsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        topProductsTable = new JTable(topProductsTableModel);
        styleTable(topProductsTable);
        
        // Inventory table
        String[] inventoryColumns = {"Product", "Category", "Stock", "Unit Price (LKR)", "Value (LKR)", "Status"};
        inventoryTableModel = new DefaultTableModel(inventoryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(inventoryTableModel);
        styleTable(inventoryTable);
        
        // Low stock table
        String[] lowStockColumns = {"Product", "Category", "Current Stock", "Status", "Value (LKR)"};
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
    }
    
    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Reports Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        summaryPanel.setBackground(BACKGROUND_COLOR);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        summaryPanel.add(totalSalesLabel);
        summaryPanel.add(totalOrdersLabel);
        summaryPanel.add(totalCustomersLabel);
        summaryPanel.add(inventoryValueLabel);
        
        // Create tabs
        createSalesTab();
        createProductsTab();
        createInventoryTab();
        createLowStockTab();
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(summaryPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void createSalesTab() {
        JPanel salesPanel = new JPanel(new BorderLayout());
        salesPanel.setBackground(BACKGROUND_COLOR);
        
        // Controls panel
        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.setBackground(BACKGROUND_COLOR);
        controlsPanel.add(new JLabel("From:"));
        controlsPanel.add(startDateChooser);
        controlsPanel.add(new JLabel("To:"));
        controlsPanel.add(endDateChooser);
        
        JButton refreshSalesButton = new JButton("Refresh Sales Data");
        refreshSalesButton.setBackground(PRIMARY_COLOR);
        refreshSalesButton.setForeground(Color.WHITE);
        refreshSalesButton.setFocusPainted(false);
        refreshSalesButton.addActionListener(e -> loadSalesData());
        controlsPanel.add(refreshSalesButton);
        
        salesPanel.add(controlsPanel, BorderLayout.NORTH);
        salesPanel.add(new JScrollPane(salesTable), BorderLayout.CENTER);
        
        tabbedPane.addTab("Sales Report", salesPanel);
    }
    
    private void createProductsTab() {
        JPanel productsPanel = new JPanel(new BorderLayout());
        productsPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.setBackground(BACKGROUND_COLOR);
        
        JButton refreshProductsButton = new JButton("Refresh Top Products");
        refreshProductsButton.setBackground(PRIMARY_COLOR);
        refreshProductsButton.setForeground(Color.WHITE);
        refreshProductsButton.setFocusPainted(false);
        refreshProductsButton.addActionListener(e -> loadTopProducts());
        controlsPanel.add(refreshProductsButton);
        
        productsPanel.add(controlsPanel, BorderLayout.NORTH);
        productsPanel.add(new JScrollPane(topProductsTable), BorderLayout.CENTER);
        
        tabbedPane.addTab("Top Products", productsPanel);
    }
    
    private void createInventoryTab() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.setBackground(BACKGROUND_COLOR);
        
        JButton refreshInventoryButton = new JButton("Refresh Inventory");
        refreshInventoryButton.setBackground(PRIMARY_COLOR);
        refreshInventoryButton.setForeground(Color.WHITE);
        refreshInventoryButton.setFocusPainted(false);
        refreshInventoryButton.addActionListener(e -> loadInventoryData());
        controlsPanel.add(refreshInventoryButton);
        
        inventoryPanel.add(controlsPanel, BorderLayout.NORTH);
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        
        tabbedPane.addTab("Inventory Report", inventoryPanel);
    }
    
    private void createLowStockTab() {
        JPanel lowStockPanel = new JPanel(new BorderLayout());
        lowStockPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel controlsPanel = new JPanel(new FlowLayout());
        controlsPanel.setBackground(BACKGROUND_COLOR);
        controlsPanel.add(new JLabel("Low Stock Threshold:"));
        controlsPanel.add(lowStockThresholdSpinner);
        
        JButton refreshLowStockButton = new JButton("Refresh Low Stock");
        refreshLowStockButton.setBackground(WARNING_COLOR);
        refreshLowStockButton.setForeground(Color.WHITE);
        refreshLowStockButton.setFocusPainted(false);
        refreshLowStockButton.addActionListener(e -> loadLowStockData());
        controlsPanel.add(refreshLowStockButton);
        
        lowStockPanel.add(controlsPanel, BorderLayout.NORTH);
        lowStockPanel.add(new JScrollPane(lowStockTable), BorderLayout.CENTER);
        
        tabbedPane.addTab("Low Stock Alert", lowStockPanel);
    }
    
    private void setupEventHandlers() {
        // Event handlers are set up in the create methods above
    }
    
    private void loadDashboardData() {
        loadSummaryData();
        loadSalesData();
        loadTopProducts();
        loadInventoryData();
        loadLowStockData();
    }
    
    private void loadSummaryData() {
        try {
            // Get today's sales
            Date today = Date.valueOf(LocalDate.now());
            SalesReport todayReport = reportsController.getDailySalesReport(today);
            
            // Get customer statistics
            Object[] customerStats = reportsController.getCustomerStatistics();
            
            // Get inventory value
            BigDecimal inventoryValue = reportsController.getTotalInventoryValue();
            
            // Update labels
            totalSalesLabel.setText("Today's Sales: " + todayReport.getFormattedTotalSales());
            totalOrdersLabel.setText("Today's Orders: " + todayReport.getTotalOrders());
            totalCustomersLabel.setText("Total Customers: " + customerStats[0]);
            inventoryValueLabel.setText("Inventory Value: LKR " + String.format("%.2f", inventoryValue));
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading summary data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSalesData() {
        try {
            salesTableModel.setRowCount(0);
            
            Date startDate = new Date(startDateChooser.getDate().getTime());
            Date endDate = new Date(endDateChooser.getDate().getTime());
            
            List<SalesReport> reports = reportsController.getSalesReportByDateRange(startDate, endDate);
            
            for (SalesReport report : reports) {
                Object[] row = {
                    report.getReportDate(),
                    report.getTotalOrders(),
                    String.format("%.2f", report.getTotalSales()),
                    String.format("%.2f", report.getAverageOrderValue()),
                    report.getTotalCustomers()
                };
                salesTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sales data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTopProducts() {
        try {
            topProductsTableModel.setRowCount(0);
            
            List<ProductSalesReport> reports = reportsController.getTopSellingProducts(20);
            
            for (ProductSalesReport report : reports) {
                Object[] row = {
                    report.getProductName(),
                    report.getCategoryName(),
                    report.getQuantitySold(),
                    String.format("%.2f", report.getTotalRevenue()),
                    String.format("%.2f", report.getAveragePrice()),
                    report.getTotalOrders()
                };
                topProductsTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading top products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadInventoryData() {
        try {
            inventoryTableModel.setRowCount(0);
            
            List<InventoryReport> reports = reportsController.getInventoryReport();
            
            for (InventoryReport report : reports) {
                Object[] row = {
                    report.getProductName(),
                    report.getCategoryName(),
                    report.getCurrentStock(),
                    String.format("%.2f", report.getUnitPrice()),
                    String.format("%.2f", report.getInventoryValue()),
                    report.getStockStatus()
                };
                inventoryTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadLowStockData() {
        try {
            lowStockTableModel.setRowCount(0);
            
            int threshold = (Integer) lowStockThresholdSpinner.getValue();
            List<InventoryReport> reports = reportsController.getLowStockReport(threshold);
            
            for (InventoryReport report : reports) {
                Object[] row = {
                    report.getProductName(),
                    report.getCategoryName(),
                    report.getCurrentStock(),
                    report.getStockStatus(),
                    String.format("%.2f", report.getInventoryValue())
                };
                lowStockTableModel.addRow(row);
            }
            
            // Show alert if there are low stock items
            if (!reports.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    reports.size() + " products are running low on stock!", 
                    "Low Stock Alert", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading low stock data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Simple date chooser implementation
class JDateChooser extends JPanel {
    private JTextField dateField;
    private java.util.Date selectedDate;
    
    public JDateChooser() {
        setLayout(new BorderLayout());
        dateField = new JTextField(10);
        dateField.setEditable(false);
        add(dateField, BorderLayout.CENTER);
        
        JButton calendarButton = new JButton("📅");
        calendarButton.addActionListener(e -> showDatePicker());
        add(calendarButton, BorderLayout.EAST);
        
        setDate(new java.util.Date());
    }
    
    private void showDatePicker() {
        // Simple date input dialog
        String dateStr = JOptionPane.showInputDialog(this, "Enter date (YYYY-MM-DD):", 
            selectedDate != null ? selectedDate.toString().substring(0, 10) : "");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            try {
                selectedDate = Date.valueOf(dateStr);
                updateDisplay();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void setDate(java.util.Date date) {
        this.selectedDate = date;
        updateDisplay();
    }
    
    public java.util.Date getDate() {
        return selectedDate;
    }
    
    private void updateDisplay() {
        if (selectedDate != null) {
            dateField.setText(selectedDate.toString().substring(0, 10));
        }
    }
}
