package view;

import controller.CartController;
import controller.OrderController;
import controller.AuthController;
import model.CartItem;
import model.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart view for customers
 */
public class ShoppingCartView extends JFrame {
    
    private CartController cartController;
    private OrderController orderController;
    private AuthController authController;
    
    // UI Components
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton updateButton, removeButton, clearCartButton, checkoutButton;
    private JTextArea shippingAddressArea;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    
    public ShoppingCartView(CartController cartController, AuthController authController) {
        this.cartController = cartController;
        this.authController = authController;
        this.orderController = new OrderController();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCartItems();
    }
    
    private void initializeComponents() {
        setTitle("Shopping Cart - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Initialize table
        String[] columnNames = {"Product", "Price (LKR)", "Quantity", "Total (LKR)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only quantity column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Integer.class;
                return String.class;
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setBackground(PRIMARY_COLOR);
        cartTable.getTableHeader().setForeground(Color.WHITE);
        
        // Initialize buttons
        updateButton = new JButton("Update Quantity");
        removeButton = new JButton("Remove Item");
        clearCartButton = new JButton("Clear Cart");
        checkoutButton = new JButton("Proceed to Checkout");
        
        // Style buttons
        JButton[] buttons = {updateButton, removeButton, clearCartButton, checkoutButton};
        for (JButton button : buttons) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
        }
        
        checkoutButton.setBackground(new Color(34, 139, 34)); // Green for checkout
        
        // Initialize labels
        totalLabel = new JLabel("Total: LKR 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(PRIMARY_COLOR);
        
        // Initialize shipping address
        shippingAddressArea = new JTextArea(3, 30);
        shippingAddressArea.setBorder(BorderFactory.createTitledBorder("Shipping Address"));
        shippingAddressArea.setLineWrap(true);
        shippingAddressArea.setWrapStyleWord(true);
        
        // Set default address if user is logged in
        if (authController.isLoggedIn() && authController.getCurrentUser().getAddress() != null) {
            shippingAddressArea.setText(authController.getCurrentUser().getAddress());
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Center - Cart table
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cart Items"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearCartButton);
        
        // Total and checkout panel
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(BACKGROUND_COLOR);
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(BACKGROUND_COLOR);
        totalPanel.add(totalLabel);
        
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBackground(BACKGROUND_COLOR);
        addressPanel.add(new JScrollPane(shippingAddressArea), BorderLayout.CENTER);
        
        JPanel checkoutButtonPanel = new JPanel(new FlowLayout());
        checkoutButtonPanel.setBackground(BACKGROUND_COLOR);
        checkoutButtonPanel.add(checkoutButton);
        
        checkoutPanel.add(totalPanel, BorderLayout.NORTH);
        checkoutPanel.add(addressPanel, BorderLayout.CENTER);
        checkoutPanel.add(checkoutButtonPanel, BorderLayout.SOUTH);
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(checkoutPanel, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        updateButton.addActionListener(e -> updateQuantity());
        removeButton.addActionListener(e -> removeItem());
        clearCartButton.addActionListener(e -> clearCart());
        checkoutButton.addActionListener(e -> checkout());
        
        cartTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = cartTable.getSelectedRow() >= 0;
                updateButton.setEnabled(hasSelection);
                removeButton.setEnabled(hasSelection);
            }
        });
    }
    
    private void loadCartItems() {
        tableModel.setRowCount(0);
        List<CartItem> items = cartController.getCartItems();
        
        for (CartItem item : items) {
            Object[] row = {
                item.getProductName(),
                String.format("%.2f", item.getUnitPrice()),
                item.getQuantity(),
                String.format("%.2f", item.getTotalPrice())
            };
            tableModel.addRow(row);
        }
        
        updateTotal();
        
        // Enable/disable buttons based on cart state
        boolean hasItems = !items.isEmpty();
        clearCartButton.setEnabled(hasItems);
        checkoutButton.setEnabled(hasItems);
        updateButton.setEnabled(false);
        removeButton.setEnabled(false);
    }
    
    private void updateTotal() {
        BigDecimal total = cartController.getTotalPrice();
        totalLabel.setText("Total: LKR " + String.format("%.2f", total));
    }
    
    private void updateQuantity() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int newQuantity = (Integer) tableModel.getValueAt(selectedRow, 2);
                if (newQuantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                List<CartItem> items = cartController.getCartItems();
                CartItem item = items.get(selectedRow);
                
                if (cartController.updateQuantity(item.getProductId(), newQuantity)) {
                    loadCartItems();
                    JOptionPane.showMessageDialog(this, "Quantity updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update quantity. Check stock availability.", "Error", JOptionPane.ERROR_MESSAGE);
                    loadCartItems(); // Reload to reset the quantity
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity value.", "Error", JOptionPane.ERROR_MESSAGE);
                loadCartItems();
            }
        }
    }
    
    private void removeItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            List<CartItem> items = cartController.getCartItems();
            CartItem item = items.get(selectedRow);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove " + item.getProductName() + " from cart?",
                "Confirm Remove", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (cartController.removeFromCart(item.getProductId())) {
                    loadCartItems();
                    JOptionPane.showMessageDialog(this, "Item removed from cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void clearCart() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Clear all items from cart?",
            "Confirm Clear Cart", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartController.clearCart();
            loadCartItems();
            JOptionPane.showMessageDialog(this, "Cart cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void checkout() {
        if (!authController.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please login to proceed with checkout.", "Login Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cartController.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String shippingAddress = shippingAddressArea.getText().trim();
        if (shippingAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter shipping address.", "Address Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate stock before checkout
        if (!cartController.validateStock()) {
            List<CartItem> outOfStock = cartController.getOutOfStockItems();
            StringBuilder message = new StringBuilder("The following items are out of stock:\n");
            for (CartItem item : outOfStock) {
                message.append("- ").append(item.getProductName()).append("\n");
            }
            message.append("\nPlease update your cart and try again.");
            JOptionPane.showMessageDialog(this, message.toString(), "Stock Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create order
        Order order = new Order();
        order.setUserId(authController.getCurrentUser().getUserId());
        order.setTotalAmount(cartController.getTotalPrice());
        order.setShippingAddress(shippingAddress);
        order.setOrderStatus(Order.OrderStatus.PENDING);
        
        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartController.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItems.add(orderItem);
        }
        
        // Process order
        if (orderController.createOrder(order, orderItems)) {
            cartController.clearCart();
            loadCartItems();
            
            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\nOrder ID: " + order.getOrderId() + 
                "\nTotal: LKR " + String.format("%.2f", order.getTotalAmount()),
                "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            
            // Close cart window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to place order. Please try again.", "Order Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
