package view;

import controller.AuthController;
import controller.CartController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Customer dashboard view for browsing and purchasing products
 */
public class CustomerDashboardView extends JFrame {
    
    private AuthController authController;
    private CartController cartController;
    private JLabel welcomeLabel;
    private JButton browseProductsButton;
    private JButton myOrdersButton;
    private JButton myProfileButton;
    private JButton cartButton;
    private JButton logoutButton;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color ACCENT_COLOR = new Color(255, 215, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color TEXT_COLOR = new Color(101, 67, 33);
    private static final Color CARD_COLOR = Color.WHITE;
    
    public CustomerDashboardView() {
        authController = new AuthController();
        cartController = new CartController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    public CustomerDashboardView(AuthController authController) {
        this.authController = authController;
        this.cartController = new CartController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Customer Dashboard - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Welcome label
        String customerName = authController.getCurrentUser() != null ? 
                             authController.getCurrentUser().getFullName() : "Customer";
        welcomeLabel = new JLabel("Welcome, " + customerName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 22));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        // Create buttons
        browseProductsButton = createDashboardButton("Browse Products", "Explore our Sri Lankan heritage collection");
        myOrdersButton = createDashboardButton("My Orders", "View your order history and status");
        myProfileButton = createDashboardButton("My Profile", "Update your account information");
        cartButton = createDashboardButton("Shopping Cart", "View and manage your cart items");
        logoutButton = createLogoutButton();
    }
    
    private JButton createDashboardButton(String title, String description) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(300, 70));
        button.setBackground(CARD_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        // Description label
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_COLOR);
        
        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);
        
        return button;
    }
    
    private JButton createLogoutButton() {
        JButton button = new JButton("Logout");
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(700, 90));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        
        JLabel titleLabel = new JLabel("Thambapanni Sobha");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Discover authentic Sri Lankan heritage products");
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subtitleLabel.setForeground(ACCENT_COLOR);
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Logout button panel
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(PRIMARY_COLOR);
        logoutPanel.add(logoutButton);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(logoutPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Welcome message
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 30, 0);
        mainPanel.add(welcomeLabel, gbc);
        
        // Customer action buttons in a 2x2 grid
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(browseProductsButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(cartButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(myOrdersButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(myProfileButton, gbc);
        
        return mainPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(SECONDARY_COLOR);
        footerPanel.setPreferredSize(new Dimension(700, 40));
        
        JLabel footerLabel = new JLabel("Experience the richness of Sri Lankan culture", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void setupEventHandlers() {
        browseProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductBrowser();
            }
        });
        
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openShoppingCart();
            }
        });
        
        myOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMyOrders();
            }
        });
        
        myProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMyProfile();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
    }
    
    private void openProductBrowser() {
        new ProductBrowserView(cartController, authController).setVisible(true);
    }
    
    private void openShoppingCart() {
        new ShoppingCartView(cartController, authController).setVisible(true);
    }
    
    private void openMyOrders() {
        new CustomerOrderHistoryView(authController).setVisible(true);
    }
    
    private void openMyProfile() {
        new CustomerProfileView().setVisible(true);
    }
    
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this, 
                                                  "Are you sure you want to logout?", 
                                                  "Confirm Logout", 
                                                  JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            authController.logout();
            new LoginView().setVisible(true);
            dispose();
        }
    }
}
