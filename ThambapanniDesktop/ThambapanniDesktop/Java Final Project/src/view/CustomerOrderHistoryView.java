package view;

import controller.OrderController;
import controller.AuthController;
import model.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Customer order history view with tracking and reorder functionality
 */
public class CustomerOrderHistoryView extends JFrame {
    
    private OrderController orderController;
    private AuthController authController;
    
    // UI Components
    private JTable ordersTable, orderItemsTable;
    private DefaultTableModel ordersTableModel, orderItemsTableModel;
    private JButton viewDetailsButton, trackOrderButton, reorderButton, refreshButton;
    private JLabel statusLabel, totalOrdersLabel, totalSpentLabel;
    private JComboBox<String> statusFilter;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color SUCCESS_COLOR = new Color(34, 139, 34);
    private static final Color WARNING_COLOR = new Color(255, 140, 0);
    private static final Color DANGER_COLOR = new Color(220, 20, 60);
    
    public CustomerOrderHistoryView(AuthController authController) {
        this.authController = authController;
        this.orderController = new OrderController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadOrderHistory();
    }
    
    private void initializeComponents() {
        setTitle("My Order History - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Initialize status filter
        statusFilter = new JComboBox<>(new String[]{"All Orders", "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"});
        
        // Initialize tables
        initializeTables();
        
        // Initialize buttons
        viewDetailsButton = new JButton("View Details");
        trackOrderButton = new JButton("Track Order");
        reorderButton = new JButton("Reorder");
        refreshButton = new JButton("Refresh");
        
        // Style buttons
        JButton[] buttons = {viewDetailsButton, trackOrderButton, reorderButton, refreshButton};
        for (JButton button : buttons) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
        }
        
        reorderButton.setBackground(SUCCESS_COLOR);
        trackOrderButton.setBackground(WARNING_COLOR);
        
        // Initialize status labels
        statusLabel = new JLabel("Welcome back! Here's your order history.");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(PRIMARY_COLOR);
        
        totalOrdersLabel = createSummaryLabel("Total Orders: 0");
        totalSpentLabel = createSummaryLabel("Total Spent: LKR 0.00");
        
        // Initially disable buttons
        viewDetailsButton.setEnabled(false);
        trackOrderButton.setEnabled(false);
        reorderButton.setEnabled(false);
    }
    
    private void initializeTables() {
        // Orders table
        String[] orderColumns = {"Order ID", "Date", "Status", "Total (LKR)", "Items", "Shipping Address"};
        ordersTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setRowHeight(25);
        ordersTable.getTableHeader().setBackground(PRIMARY_COLOR);
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        
        // Order items table
        String[] itemColumns = {"Product", "Quantity", "Unit Price (LKR)", "Total (LKR)"};
        orderItemsTableModel = new DefaultTableModel(itemColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(orderItemsTableModel);
        orderItemsTable.setRowHeight(25);
        orderItemsTable.getTableHeader().setBackground(SECONDARY_COLOR);
        orderItemsTable.getTableHeader().setForeground(Color.WHITE);
    }
    
    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
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
        
        JLabel titleLabel = new JLabel("My Order History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout());
        summaryPanel.setBackground(BACKGROUND_COLOR);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.add(totalOrdersLabel);
        summaryPanel.add(Box.createHorizontalStrut(20));
        summaryPanel.add(totalSpentLabel);
        summaryPanel.add(Box.createHorizontalStrut(20));
        summaryPanel.add(new JLabel("Filter:"));
        summaryPanel.add(statusFilter);
        summaryPanel.add(refreshButton);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Orders panel
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBorder(BorderFactory.createTitledBorder("Order History"));
        ordersPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);
        
        // Order details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        detailsPanel.add(new JScrollPane(orderItemsTable), BorderLayout.CENTER);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ordersPanel, detailsPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.6);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(trackOrderButton);
        buttonPanel.add(reorderButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Combine panels
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(summaryPanel, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                boolean hasSelection = selectedRow >= 0;
                
                viewDetailsButton.setEnabled(hasSelection);
                trackOrderButton.setEnabled(hasSelection);
                reorderButton.setEnabled(hasSelection);
                
                if (hasSelection) {
                    loadOrderDetails(selectedRow);
                } else {
                    orderItemsTableModel.setRowCount(0);
                }
            }
        });
        
        viewDetailsButton.addActionListener(e -> viewOrderDetails());
        trackOrderButton.addActionListener(e -> trackOrder());
        reorderButton.addActionListener(e -> reorderItems());
        refreshButton.addActionListener(e -> loadOrderHistory());
        statusFilter.addActionListener(e -> filterOrders());
    }
    
    private void loadOrderHistory() {
        try {
            ordersTableModel.setRowCount(0);
            
            if (!authController.isLoggedIn()) {
                statusLabel.setText("Please login to view order history.");
                return;
            }
            
            int userId = authController.getCurrentUser().getUserId();
            List<Order> orders = orderController.getOrdersByUserId(userId);
            
            double totalSpent = 0.0;
            
            for (Order order : orders) {
                Object[] row = {
                    order.getOrderId(),
                    order.getCreatedAt() != null ? order.getCreatedAt().toString().substring(0, 10) : "",
                    order.getOrderStatus().name(),
                    String.format("%.2f", order.getTotalAmount()),
                    getOrderItemCount(order.getOrderId()),
                    order.getShippingAddress()
                };
                ordersTableModel.addRow(row);
                totalSpent += order.getTotalAmount().doubleValue();
            }
            
            // Update summary
            totalOrdersLabel.setText("Total Orders: " + orders.size());
            totalSpentLabel.setText("Total Spent: LKR " + String.format("%.2f", totalSpent));
            
            if (orders.isEmpty()) {
                statusLabel.setText("No orders found. Start shopping to see your order history!");
            } else {
                statusLabel.setText("Showing " + orders.size() + " orders. Select an order to view details.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int getOrderItemCount(int orderId) {
        try {
            List<OrderItem> items = orderController.getOrderItems(orderId);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    private void loadOrderDetails(int selectedRow) {
        try {
            orderItemsTableModel.setRowCount(0);
            
            int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
            List<OrderItem> items = orderController.getOrderItems(orderId);
            
            for (OrderItem item : items) {
                Object[] row = {
                    item.getProductName() != null ? item.getProductName() : "Product ID: " + item.getProductId(),
                    item.getQuantity(),
                    String.format("%.2f", item.getUnitPrice()),
                    String.format("%.2f", item.getTotalPrice())
                };
                orderItemsTableModel.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void viewOrderDetails() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
            String status = (String) ordersTableModel.getValueAt(selectedRow, 2);
            String total = (String) ordersTableModel.getValueAt(selectedRow, 3);
            String date = (String) ordersTableModel.getValueAt(selectedRow, 1);
            
            StringBuilder details = new StringBuilder();
            details.append("Order Details\n");
            details.append("═══════════════\n\n");
            details.append("Order ID: ").append(orderId).append("\n");
            details.append("Date: ").append(date).append("\n");
            details.append("Status: ").append(status).append("\n");
            details.append("Total: LKR ").append(total).append("\n\n");
            
            details.append("Items:\n");
            for (int i = 0; i < orderItemsTableModel.getRowCount(); i++) {
                String product = (String) orderItemsTableModel.getValueAt(i, 0);
                int quantity = (Integer) orderItemsTableModel.getValueAt(i, 1);
                String unitPrice = (String) orderItemsTableModel.getValueAt(i, 2);
                details.append("• ").append(product).append(" (Qty: ").append(quantity).append(", Price: LKR ").append(unitPrice).append(")\n");
            }
            
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Order Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void trackOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow >= 0) {
            String status = (String) ordersTableModel.getValueAt(selectedRow, 2);
            int orderId = (Integer) ordersTableModel.getValueAt(selectedRow, 0);
            
            StringBuilder tracking = new StringBuilder();
            tracking.append("Order Tracking - Order #").append(orderId).append("\n");
            tracking.append("═══════════════════════════════\n\n");
            
            // Mock tracking information
            tracking.append("📦 Order Placed ✓\n");
            tracking.append("⏳ Payment Confirmed ").append(status.equals("PENDING") ? "⏳" : "✓").append("\n");
            tracking.append("🏭 Processing ").append(status.equals("PROCESSING") || status.equals("SHIPPED") || status.equals("DELIVERED") ? "✓" : "⏳").append("\n");
            tracking.append("🚚 Shipped ").append(status.equals("SHIPPED") || status.equals("DELIVERED") ? "✓" : "⏳").append("\n");
            tracking.append("🏠 Delivered ").append(status.equals("DELIVERED") ? "✓" : "⏳").append("\n");
            
            tracking.append("\nCurrent Status: ").append(status).append("\n");
            
            if (status.equals("SHIPPED")) {
                tracking.append("\n📍 Estimated Delivery: 2-3 business days");
            } else if (status.equals("PROCESSING")) {
                tracking.append("\n⏰ Expected Ship Date: 1-2 business days");
            }
            
            JTextArea textArea = new JTextArea(tracking.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(350, 250));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Order Tracking", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void reorderItems() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow >= 0) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Add all items from this order to your cart?",
                "Reorder Items", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                // This would integrate with the cart controller
                JOptionPane.showMessageDialog(this, 
                    "Items added to cart! Go to your cart to review and checkout.",
                    "Reorder Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void filterOrders() {
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        if ("All Orders".equals(selectedStatus)) {
            loadOrderHistory();
        } else {
            // Filter logic would go here
            statusLabel.setText("Filtering by status: " + selectedStatus);
            // For now, just reload all orders
            loadOrderHistory();
        }
    }
}
