package view;

import controller.ProductController;
import controller.CategoryController;
import model.Category;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

/**
 * Product management view for admin operations
 */
public class ProductManagementView extends JFrame {

    private ProductController productController;
    private CategoryController categoryController;

    // UI Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, stockField, originField;
    private JTextArea descriptionArea;
    private JComboBox<Category> categoryCombo;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public ProductManagementView() {
        productController = new ProductController();
        categoryController = new CategoryController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadProducts();
        loadCategories();
    }

    private void initializeComponents() {
        setTitle("Product Management - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize table
        String[] columnNames = {"ID", "Name", "Category", "Price (LKR)", "Stock", "Origin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProduct();
            }
        });

        // Initialize form fields
        nameField = new JTextField(20);
        priceField = new JTextField(10);
        stockField = new JTextField(10);
        originField = new JTextField(15);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        categoryCombo = new JComboBox<>();

        // Initialize buttons
        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        refreshButton = new JButton("Refresh");

        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(refreshButton);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Products"));

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel tableButtonPanel = new JPanel(new FlowLayout());
        tableButtonPanel.add(refreshButton);
        tablePanel.add(tableButtonPanel, BorderLayout.SOUTH);

        // Form panel
        JPanel formPanel = createFormPanel();

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, formPanel);
        splitPane.setDividerLocation(750);
        splitPane.setResizeWeight(0.6);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        formPanel.setBackground(BACKGROUND_COLOR);

        // Form fields
        formPanel.add(createFieldPanel("Product Name:", nameField));
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createFieldPanel("Category:", categoryCombo));
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createFieldPanel("Price (LKR):", priceField));
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createFieldPanel("Stock Quantity:", stockField));
        formPanel.add(Box.createVerticalStrut(10));

        formPanel.add(createFieldPanel("Origin:", originField));
        formPanel.add(Box.createVerticalStrut(10));

        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descLabel);
        formPanel.add(Box.createVerticalStrut(5));

        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        formPanel.add(descScrollPane);
        formPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(buttonPanel);

        return formPanel;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JTextField) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        }
        panel.add(field);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> {
            loadProducts();
            clearForm();
        });
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productController.getAllProducts();

        for (Product product : products) {
            Object[] row = {
                product.getProductId(),
                product.getProductName(),
                product.getCategoryName(),
                String.format("%.2f", product.getPrice()),
                product.getStockQuantity(),
                product.getOrigin()
            };
            tableModel.addRow(row);
        }
    }

    private void loadCategories() {
        categoryCombo.removeAllItems();
        List<Category> categories = categoryController.getAllCategories();
        for (Category category : categories) {
            categoryCombo.addItem(category);
        }
    }

    private void loadSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Product product = productController.getProductById(productId);

            if (product != null) {
                nameField.setText(product.getProductName());
                priceField.setText(product.getPrice().toString());
                stockField.setText(String.valueOf(product.getStockQuantity()));
                originField.setText(product.getOrigin());
                descriptionArea.setText(product.getDescription());

                // Select category
                for (int i = 0; i < categoryCombo.getItemCount(); i++) {
                    Category category = categoryCombo.getItemAt(i);
                    if (category.getCategoryId() == product.getCategoryId()) {
                        categoryCombo.setSelectedIndex(i);
                        break;
                    }
                }

                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        } else {
            clearForm();
        }
    }

    private void addProduct() {
        if (validateForm()) {
            try {
                Product product = new Product();
                product.setProductName(nameField.getText().trim());
                product.setDescription(descriptionArea.getText().trim());
                product.setPrice(new BigDecimal(priceField.getText().trim()));
                product.setStockQuantity(Integer.parseInt(stockField.getText().trim()));
                product.setCategoryId(((Category) categoryCombo.getSelectedItem()).getCategoryId());
                product.setOrigin(originField.getText().trim());

                if (productController.createProduct(product)) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0 && validateForm()) {
            try {
                int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
                Product product = new Product();
                product.setProductId(productId);
                product.setProductName(nameField.getText().trim());
                product.setDescription(descriptionArea.getText().trim());
                product.setPrice(new BigDecimal(priceField.getText().trim()));
                product.setStockQuantity(Integer.parseInt(stockField.getText().trim()));
                product.setCategoryId(((Category) categoryCombo.getSelectedItem()).getCategoryId());
                product.setOrigin(originField.getText().trim());

                if (productController.updateProduct(product)) {
                    JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String productName = (String) tableModel.getValueAt(selectedRow, 1);

            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the product '" + productName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (productController.deleteProduct(productId)) {
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        if (categoryCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a category.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }

        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "Stock quantity cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                stockField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid stock quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            stockField.requestFocus();
            return false;
        }

        return true;
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        priceField.setText("");
        stockField.setText("");
        originField.setText("");
        categoryCombo.setSelectedIndex(-1);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        productTable.clearSelection();
    }
}
