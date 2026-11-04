package view;

import controller.AuthController;
import dao.OrderDAO;
import dao.OrderItemDAO;
import model.Order;
import model.OrderItem;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Customer orders view
 */
public class CustomerOrdersView extends JFrame {

    private AuthController authController;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    // UI Components
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    private JTable orderItemsTable;
    private DefaultTableModel itemsTableModel;
    private JButton refreshButton, viewDetailsButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public CustomerOrdersView() {
        authController = new AuthController();
        orderDAO = new OrderDAO();
        orderItemDAO = new OrderItemDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCustomerOrders();
    }

    private void initializeComponents() {
        setTitle("My Orders - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize order table
        String[] orderColumns = {"Order ID", "Total (LKR)", "Status", "Order Date"};
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

        // Initialize buttons
        refreshButton = new JButton("Refresh");
        viewDetailsButton = new JButton("View Details");

        styleButton(refreshButton);
        styleButton(viewDetailsButton);

        viewDetailsButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        User currentUser = authController.getCurrentUser();
        String customerName = currentUser != null ? currentUser.getFullName() : "Customer";

        JLabel titleLabel = new JLabel("My Orders - " + customerName);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Orders table panel
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.setBorder(BorderFactory.createTitledBorder("My Orders"));

        JScrollPane ordersScrollPane = new JScrollPane(orderTable);
        ordersScrollPane.setPreferredSize(new Dimension(860, 200));
        ordersPanel.add(ordersScrollPane, BorderLayout.CENTER);

        JPanel orderButtonPanel = new JPanel(new FlowLayout());
        orderButtonPanel.add(refreshButton);
        orderButtonPanel.add(viewDetailsButton);
        ordersPanel.add(orderButtonPanel, BorderLayout.SOUTH);

        // Order items panel
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Order Items"));

        JScrollPane itemsScrollPane = new JScrollPane(orderItemsTable);
        itemsScrollPane.setPreferredSize(new Dimension(860, 200));
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ordersPanel, itemsPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        refreshButton.addActionListener(e -> loadCustomerOrders());
        viewDetailsButton.addActionListener(e -> viewOrderDetails());

        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = orderTable.getSelectedRow() >= 0;
                viewDetailsButton.setEnabled(hasSelection);
                loadOrderItems();
            }
        });
    }

    private void loadCustomerOrders() {
        orderTableModel.setRowCount(0);

        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            List<Order> orders = orderDAO.getOrdersByUserId(currentUser.getUserId());

            for (Order order : orders) {
                Object[] row = {
                    order.getOrderId(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getOrderStatus(),
                    order.getCreatedAt() != null ? order.getCreatedAt().toString().substring(0, 16) : ""
                };
                orderTableModel.addRow(row);
            }

            if (orders.isEmpty()) {
                // Show message if no orders
                JLabel noOrdersLabel = new JLabel("You haven't placed any orders yet.", SwingConstants.CENTER);
                noOrdersLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                noOrdersLabel.setForeground(Color.GRAY);
            }
        }
    }

    private void loadOrderItems() {
        itemsTableModel.setRowCount(0);

        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);

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

    private void viewOrderDetails() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (Integer) orderTableModel.getValueAt(selectedRow, 0);
            Order order = orderDAO.getOrderById(orderId);

            if (order != null) {
                StringBuilder details = new StringBuilder();
                details.append("Order ID: ").append(order.getOrderId()).append("\n");
                details.append("Total Amount: LKR ").append(String.format("%.2f", order.getTotalAmount())).append("\n");
                details.append("Status: ").append(order.getOrderStatus()).append("\n");
                details.append("Order Date: ").append(order.getCreatedAt()).append("\n");
                details.append("Shipping Address: ").append(order.getShippingAddress()).append("\n\n");

                details.append("Order Items:\n");
                List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);
                for (OrderItem item : items) {
                    details.append("- ").append(item.getProductName())
                           .append(" (Qty: ").append(item.getQuantity())
                           .append(", Unit Price: LKR ").append(String.format("%.2f", item.getUnitPrice()))
                           .append(", Total: LKR ").append(String.format("%.2f", item.getTotalPrice()))
                           .append(")\n");
                }

                // Add order status information
                details.append("\nOrder Status Information:\n");
                switch (order.getOrderStatus()) {
                    case PENDING:
                        details.append("Your order is being processed.");
                        break;
                    case PROCESSING:
                        details.append("Your order is being prepared for shipment.");
                        break;
                    case CONFIRMED:
                        details.append("Your order has been confirmed and is being prepared.");
                        break;
                    case SHIPPED:
                        details.append("Your order has been shipped and is on its way.");
                        break;
                    case DELIVERED:
                        details.append("Your order has been delivered successfully.");
                        break;
                    case CANCELLED:
                        details.append("Your order has been cancelled.");
                        break;
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
}
