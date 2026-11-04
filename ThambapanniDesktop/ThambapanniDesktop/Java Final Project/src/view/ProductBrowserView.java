package view;

import controller.ProductController;
import controller.CategoryController;
import controller.CartController;
import controller.AuthController;
import model.Category;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Product browser view for customers
 */
public class ProductBrowserView extends JFrame {

    private ProductController productController;
    private CategoryController categoryController;
    private CartController cartController;
    private AuthController authController;

    // UI Components
    private JComboBox<Category> categoryFilter;
    private JTextField searchField;
    private JButton searchButton, clearButton, addToCartButton, viewCartButton;
    private JPanel productsPanel;
    private JScrollPane scrollPane;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color ACCENT_COLOR = new Color(255, 215, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public ProductBrowserView() {
        productController = new ProductController();
        categoryController = new CategoryController();
        cartController = new CartController();
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCategories();
        loadProducts();
    }

    public ProductBrowserView(CartController cartController, AuthController authController) {
        this.productController = new ProductController();
        this.categoryController = new CategoryController();
        this.cartController = cartController;
        this.authController = authController;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCategories();
        loadProducts();
    }

    private void initializeComponents() {
        setTitle("Browse Products - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize components
        categoryFilter = new JComboBox<>();
        Category allCategories = new Category("All Categories", "Show all products");
        allCategories.setCategoryId(0);
        categoryFilter.addItem(allCategories);

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear");
        addToCartButton = new JButton("Add to Cart");
        viewCartButton = new JButton("View Cart");

        styleButton(searchButton);
        styleButton(clearButton);
        styleButton(addToCartButton);
        styleButton(viewCartButton);

        // Special styling for cart buttons
        addToCartButton.setBackground(new Color(34, 139, 34)); // Green
        viewCartButton.setBackground(new Color(255, 140, 0)); // Orange

        productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
        productsPanel.setBackground(BACKGROUND_COLOR);

        scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Browse Sri Lankan Heritage Products");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(BACKGROUND_COLOR);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        filterPanel.add(clearButton);
        filterPanel.add(Box.createHorizontalStrut(30));
        filterPanel.add(viewCartButton);

        // Update cart button text with item count
        updateCartButtonText();

        add(headerPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Adjust layout weights
        scrollPane.setPreferredSize(new Dimension(1000, 500));
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearSearch());
        viewCartButton.addActionListener(e -> openShoppingCart());

        categoryFilter.addActionListener(e -> filterByCategory());

        // Enter key support for search
        searchField.addActionListener(e -> performSearch());
    }

    private void loadCategories() {
        List<Category> categories = categoryController.getAllCategories();
        for (Category category : categories) {
            categoryFilter.addItem(category);
        }
    }

    private void loadProducts() {
        List<Product> products = productController.getAllProducts();
        displayProducts(products);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Product> products = productController.searchProducts(searchTerm);
            displayProducts(products);
        } else {
            loadProducts();
        }
    }

    private void clearSearch() {
        searchField.setText("");
        categoryFilter.setSelectedIndex(0);
        loadProducts();
    }

    private void filterByCategory() {
        Category selectedCategory = (Category) categoryFilter.getSelectedItem();
        if (selectedCategory != null) {
            List<Product> products;
            if (selectedCategory.getCategoryId() == 0) {
                products = productController.getAllProducts();
            } else {
                products = productController.getProductsByCategory(selectedCategory.getCategoryId());
            }
            displayProducts(products);
        }
    }

    private void displayProducts(List<Product> products) {
        productsPanel.removeAll();

        if (products.isEmpty()) {
            JLabel noProductsLabel = new JLabel("No products found.", SwingConstants.CENTER);
            noProductsLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
            noProductsLabel.setForeground(Color.GRAY);
            productsPanel.add(noProductsLabel);
        } else {
            for (Product product : products) {
                JPanel productCard = createProductCard(product);
                productsPanel.add(productCard);
                productsPanel.add(Box.createVerticalStrut(10));
            }
        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Product info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Product name
        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        infoPanel.add(nameLabel, gbc);

        // Category
        JLabel categoryLabel = new JLabel("Category: " + product.getCategoryName());
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        categoryLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 3, 20);
        infoPanel.add(categoryLabel, gbc);

        // Origin
        if (product.getOrigin() != null && !product.getOrigin().isEmpty()) {
            JLabel originLabel = new JLabel("Origin: " + product.getOrigin());
            originLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            originLabel.setForeground(Color.GRAY);
            gbc.gridx = 1; gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 3, 0);
            infoPanel.add(originLabel, gbc);
        }

        // Description
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            JTextArea descArea = new JTextArea(product.getDescription());
            descArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
            descArea.setForeground(Color.DARK_GRAY);
            descArea.setBackground(Color.WHITE);
            descArea.setEditable(false);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setRows(2);

            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(5, 0, 0, 0);
            infoPanel.add(descArea, gbc);
        }

        // Price and stock panel
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pricePanel.setBackground(Color.WHITE);

        // Price
        JLabel priceLabel = new JLabel(product.getFormattedPrice());
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        priceLabel.setForeground(PRIMARY_COLOR);
        pricePanel.add(priceLabel);

        // Stock status
        JLabel stockLabel;
        if (product.isOutOfStock()) {
            stockLabel = new JLabel("Out of Stock");
            stockLabel.setForeground(Color.RED);
        } else if (product.isLowStock()) {
            stockLabel = new JLabel("Low Stock (" + product.getStockQuantity() + ")");
            stockLabel.setForeground(Color.ORANGE);
        } else {
            stockLabel = new JLabel("In Stock (" + product.getStockQuantity() + ")");
            stockLabel.setForeground(Color.GREEN.darker());
        }
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        pricePanel.add(Box.createHorizontalStrut(15));
        pricePanel.add(stockLabel);

        // Add to cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(ACCENT_COLOR);
        addToCartButton.setForeground(PRIMARY_COLOR);
        addToCartButton.setFocusPainted(false);
        addToCartButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addToCartButton.setEnabled(!product.isOutOfStock());

        // Add functionality to add to cart button
        addToCartButton.addActionListener(e -> addProductToCart(product));

        pricePanel.add(Box.createHorizontalStrut(10));
        pricePanel.add(addToCartButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(pricePanel, BorderLayout.EAST);

        return card;
    }

    /**
     * Add product to cart
     */
    private void addProductToCart(Product product) {
        if (!authController.isLoggedIn()) {
            int choice = JOptionPane.showConfirmDialog(this,
                "You need to login to add items to cart. Would you like to login now?",
                "Login Required", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // Open login dialog or redirect to login
                JOptionPane.showMessageDialog(this, "Please login from the main menu.", "Login", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        }

        // Ask for quantity
        String quantityStr = JOptionPane.showInputDialog(this,
            "Enter quantity for " + product.getProductName() + ":", "Add to Cart", JOptionPane.QUESTION_MESSAGE);

        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Invalid Quantity", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (quantity > product.getStockQuantity()) {
                    JOptionPane.showMessageDialog(this,
                        "Only " + product.getStockQuantity() + " items available in stock.",
                        "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (cartController.addToCart(product.getProductId(), quantity)) {
                    JOptionPane.showMessageDialog(this,
                        quantity + " x " + product.getProductName() + " added to cart!",
                        "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
                    updateCartButtonText();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add item to cart.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Open shopping cart window
     */
    private void openShoppingCart() {
        if (!authController.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please login to view your cart.", "Login Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ShoppingCartView cartView = new ShoppingCartView(cartController, authController);
        cartView.setVisible(true);

        // Update cart button text when cart window is closed
        cartView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                updateCartButtonText();
            }
        });
    }

    /**
     * Update cart button text with item count
     */
    private void updateCartButtonText() {
        if (cartController != null) {
            int itemCount = cartController.getTotalItemCount();
            if (itemCount > 0) {
                viewCartButton.setText("Cart (" + itemCount + ")");
            } else {
                viewCartButton.setText("Cart");
            }
        }
    }
}
