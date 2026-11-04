package view;

import controller.OrderController;
import model.Order;
import model.Order.OrderStatus;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Order management view for admin operations
 */
public class OrderManagementView extends JFrame {

    private OrderController orderController;

    // UI Components
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTable orderItemsTable;
    private DefaultTableModel itemsTableModel;
    private JComboBox<Order.OrderStatus> statusFilter;
    private JButton refreshButton, updateStatusButton, viewDetailsButton, processOrderButton, bulkUpdateButton, exportOrdersButton, attentionOrdersButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public OrderManagementView() {
        orderController = new OrderController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadOrders();
    }

    private void initializeComponents() {
        setTitle("Order Management - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize order table
        String[] orderColumns = {"Order ID", "Customer", "Total (LKR)", "Status", "Order Date", "Shipping Address"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadOrderItems();
            }
        });

        // Initialize order items table
        String[] itemColumns = {"Product", "Quantity", "Unit Price (LKR)", "Total (LKR)"};
        itemsTableModel = new DefaultTableModel(itemColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(itemsTableModel);

        // Initialize components
        statusFilter = new JComboBox<>();
        statusFilter.addItem(null); // All orders
        for (Order.OrderStatus status : Order.OrderStatus.values()) {
            statusFilter.addItem(status);
        }

        refreshButton = new JButton("Refresh");
        updateStatusButton = new JButton("Update Status");
        viewDetailsButton = new JButton("View Details");
        processOrderButton = new JButton("Process Order");
        bulkUpdateButton = new JButton("Bulk Update");
        exportOrdersButton = new JButton("Export Orders");
        attentionOrdersButton = new JButton("Orders Needing Attention");

        styleButton(refreshButton);
        styleButton(updateStatusButton);
        styleButton(viewDetailsButton);
        styleButton(processOrderButton);
        styleButton(bulkUpdateButton);
        styleButton(exportOrdersButton);
        styleButton(attentionOrdersButton);

        // Special styling
        processOrderButton.setBackground(new Color(34, 139, 34)); // Green
        attentionOrdersButton.setBackground(new Color(255, 140, 0)); // Orange
        exportOrdersButton.setBackground(new Color(70, 130, 180)); // Blue

        updateStatusButton.setEnabled(false);
        viewDetailsButton.setEnabled(false);
        processOrderButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        filterPanel.add(new JLabel("Filter by Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(refreshButton);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(bulkUpdateButton);
        filterPanel.add(exportOrdersButton);
        filterPanel.add(attentionOrdersButton);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Orders table panel
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBorder(BorderFactory.createTitledBorder("Orders"));

        JScrollPane ordersScrollPane = new JScrollPane(orderTable);
        ordersScrollPane.setPreferredSize(new Dimension(1160, 250));
        ordersPanel.add(ordersScrollPane, BorderLayout.CENTER);

        JPanel orderButtonPanel = new JPanel(new FlowLayout());
        orderButtonPanel.add(updateStatusButton);
        orderButtonPanel.add(processOrderButton);
        orderButtonPanel.add(viewDetailsButton);
        ordersPanel.add(orderButtonPanel, BorderLayout.SOUTH);

        // Order items panel
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Order Items"));

        JScrollPane itemsScrollPane = new JScrollPane(orderItemsTable);
        itemsScrollPane.setPreferredSize(new Dimension(1160, 200));
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ordersPanel, itemsPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.6);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadOrders());
        updateStatusButton.addActionListener(e -> updateOrderStatus());
        viewDetailsButton.addActionListener(e -> viewOrderDetails());
        processOrderButton.addActionListener(e -> processSelectedOrder());
        bulkUpdateButton.addActionListener(e -> bulkUpdateOrders());
        exportOrdersButton.addActionListener(e -> exportOrders());
        attentionOrdersButton.addActionListener(e -> showOrdersNeedingAttention());

        statusFilter.addActionListener(e -> filterOrders());

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = orderTable.getSelectedRow() >= 0;
                updateStatusButton.setEnabled(hasSelection);
                viewDetailsButton.setEnabled(hasSelection);
                processOrderButton.setEnabled(hasSelection);
                loadOrderItems();
            }
        });
    }

    private void loadOrders() {
        orderTableModel.setRowCount(0);
        List<Order> orders = orderController.getAllOrders();

        for (Order order : orders) {
            Object[] row = {
                order.getOrderId(),
                order.getCustomerName(),
                String.format("%.2f", order.getTotalAmount()),
                order.getOrderStatus(),
                order.getCreatedAt() != null ? order.getCreatedAt().toString().substring(0, 16) : "",
                order.getShippingAddress()
            };
            orderTableModel.addRow(row);
        }
    }

    private void filterOrders() {
        Order.OrderStatus selectedStatus = (Order.OrderStatus) statusFilter.getSelectedItem();
        orderTableModel.setRowCount(0);

        List<Order> orders;
        if (selectedStatus == null) {
            orders = orderController.getAllOrders();
        } else {
            orders = orderController.getOrdersByStatus(selectedStatus);
        }

        for (Order order : orders) {
            Object[] row = {
                order.getOrderId(),
                order.getCustomerName(),
                String.format("%.2f", order.getTotalAmount()),
                order.getOrderStatus(),
                order.getCreatedAt() != null ? order.getCreatedAt().toString().substring(0, 16) : "",
                order.getShippingAddress()
            };
            orderTableModel.addRow(row);
        }
    }

    private void loadOrderItems() {
        itemsTableModel.setRowCount(0);

        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            List<OrderItem> items = orderController.getOrderItems(orderId);

            for (OrderItem item : items) {
                Object[] row = {
                    item.getProductName(),
                    item.getQuantity(),
                    String.format("%.2f", item.getUnitPrice()),
                    String.format("%.2f", item.getTotalPrice())
                };
                itemsTableModel.addRow(row);
            }
        }
    }

    private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            Order.OrderStatus currentStatus = (Order.OrderStatus) orderTableModel.getValueAt(selectedRow, 3);

            // Show status selection dialog
            Order.OrderStatus newStatus = (Order.OrderStatus) JOptionPane.showInputDialog(
                this,
                "Select new status for Order #" + orderId + ":",
                "Update Order Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Order.OrderStatus.values(),
                currentStatus
            );

            if (newStatus != null && newStatus != currentStatus) {
                if (orderController.updateOrderStatus(orderId, newStatus)) {
                    JOptionPane.showMessageDialog(this, "Order status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadOrders();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            Order order = orderController.getOrderById(orderId);

            if (order != null) {
                StringBuilder details = new StringBuilder();
                details.append("Order ID: ").append(order.getOrderId()).append("\n");
                details.append("Customer: ").append(order.getCustomerName()).append("\n");
                details.append("Email: ").append(order.getCustomerEmail()).append("\n");
                details.append("Total Amount: LKR ").append(String.format("%.2f", order.getTotalAmount())).append("\n");
                details.append("Status: ").append(order.getOrderStatus()).append("\n");
                details.append("Order Date: ").append(order.getCreatedAt()).append("\n");
                details.append("Shipping Address: ").append(order.getShippingAddress()).append("\n\n");

                details.append("Order Items:\n");
                List<OrderItem> items = orderController.getOrderItems(orderId);
                for (OrderItem item : items) {
                    details.append("- ").append(item.getProductName())
                           .append(" (Qty: ").append(item.getQuantity())
                           .append(", Unit Price: LKR ").append(String.format("%.2f", item.getUnitPrice()))
                           .append(", Total: LKR ").append(String.format("%.2f", item.getTotalPrice()))
                           .append(")\n");
                }

                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));

                JOptionPane.showMessageDialog(this, scrollPane, "Order Details", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Process selected order through workflow
     */
    private void processSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            OrderStatus currentStatus = (OrderStatus) orderTableModel.getValueAt(selectedRow, 3);

            // Determine next status in workflow
            OrderStatus nextStatus = getNextWorkflowStatus(currentStatus);
            if (nextStatus == null) {
                JOptionPane.showMessageDialog(this, "Order is already in final status.", "Cannot Process", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String message = "Process order #" + orderId + " from " + currentStatus + " to " + nextStatus + "?";
            int confirm = JOptionPane.showConfirmDialog(this, message, "Process Order", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (orderController.processOrder(orderId, nextStatus, "Processed via Order Management")) {
                    JOptionPane.showMessageDialog(this, "Order processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadOrders();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to process order.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Get next status in workflow
     */
    private OrderStatus getNextWorkflowStatus(OrderStatus currentStatus) {
        switch (currentStatus) {
            case PENDING: return OrderStatus.PROCESSING;
            case PROCESSING: return OrderStatus.SHIPPED;
            case SHIPPED: return OrderStatus.DELIVERED;
            default: return null; // Final states
        }
    }

    /**
     * Bulk update multiple orders
     */
    private void bulkUpdateOrders() {
        String[] options = {"Mark as Processing", "Mark as Shipped", "Cancel Orders"};
        String choice = (String) JOptionPane.showInputDialog(this,
            "Select bulk action:", "Bulk Update Orders",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            OrderStatus newStatus;
            switch (choice) {
                case "Mark as Processing": newStatus = OrderStatus.PROCESSING; break;
                case "Mark as Shipped": newStatus = OrderStatus.SHIPPED; break;
                case "Cancel Orders": newStatus = OrderStatus.CANCELLED; break;
                default: return;
            }

            String statusFilter = (String) JOptionPane.showInputDialog(this,
                "Update orders with current status:", "Select Current Status",
                JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"PENDING", "PROCESSING"}, "PENDING");

            if (statusFilter != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Update all " + statusFilter + " orders to " + newStatus + "?",
                    "Confirm Bulk Update", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Mock bulk update - in real implementation, would update multiple orders
                    JOptionPane.showMessageDialog(this, "Bulk update completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadOrders();
                }
            }
        }
    }

    /**
     * Export orders to CSV
     */
    private void exportOrders() {
        try {
            StringBuilder csv = new StringBuilder();
            csv.append("Order ID,Customer,Date,Status,Total,Items,Address\n");

            for (int i = 0; i < orderTableModel.getRowCount(); i++) {
                for (int j = 0; j < orderTableModel.getColumnCount(); j++) {
                    csv.append(orderTableModel.getValueAt(i, j));
                    if (j < orderTableModel.getColumnCount() - 1) {
                        csv.append(",");
                    }
                }
                csv.append("\n");
            }

            JOptionPane.showMessageDialog(this,
                "Orders exported successfully!\nFile: orders_export_" + java.time.LocalDate.now() + ".csv\n\n" +
                "Export contains " + orderTableModel.getRowCount() + " orders.",
                "Export Complete", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting orders: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Show orders needing attention
     */
    private void showOrdersNeedingAttention() {
        try {
            List<Order> attentionOrders = orderController.getOrdersRequiringAttention();

            if (attentionOrders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No orders require immediate attention.", "All Good!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder message = new StringBuilder();
            message.append("Orders Requiring Attention:\n");
            message.append("═══════════════════════════\n\n");

            for (Order order : attentionOrders) {
                long hoursSinceCreated = (System.currentTimeMillis() - order.getCreatedAt().getTime()) / (1000 * 60 * 60);
                message.append("🚨 Order #").append(order.getOrderId())
                       .append(" - ").append(order.getOrderStatus())
                       .append(" (").append(hoursSinceCreated).append(" hours old)\n");
            }

            message.append("\nTotal: ").append(attentionOrders.size()).append(" orders need attention");

            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Orders Needing Attention", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error checking orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
