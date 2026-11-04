package view;

import controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Admin dashboard view with management capabilities
 */
public class AdminDashboardView extends JFrame {
    
    private AuthController authController;
    private JLabel welcomeLabel;
    private JButton productManagementButton;
    private JButton categoryManagementButton;
    private JButton userManagementButton;
    private JButton orderManagementButton;
    private JButton reportsButton;
    private JButton logoutButton;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color ACCENT_COLOR = new Color(255, 215, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color TEXT_COLOR = new Color(101, 67, 33);
    private static final Color CARD_COLOR = Color.WHITE;
    
    public AdminDashboardView() {
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Admin Dashboard - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Welcome label
        String adminName = authController.getCurrentUser() != null ? 
                          authController.getCurrentUser().getFullName() : "Administrator";
        welcomeLabel = new JLabel("Welcome, " + adminName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        // Create buttons
        productManagementButton = createDashboardButton("Product Management", "Manage products, inventory, and pricing");
        categoryManagementButton = createDashboardButton("Category Management", "Manage product categories");
        userManagementButton = createDashboardButton("User Management", "Manage customer accounts");
        orderManagementButton = createDashboardButton("Order Management", "View and manage customer orders");
        reportsButton = createDashboardButton("Reports & Analytics", "View sales reports and statistics");
        logoutButton = createLogoutButton();
    }
    
    private JButton createDashboardButton(String title, String description) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(350, 80));
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
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);
        
        JLabel titleLabel = new JLabel("Thambapanni Sobha Admin Panel");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Manage your Sri Lankan heritage e-commerce platform");
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
        
        // Management buttons in a 2x3 grid
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(productManagementButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(categoryManagementButton, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(userManagementButton, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(orderManagementButton, gbc);
        
        // Row 3 (centered)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(reportsButton, gbc);
        
        return mainPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(SECONDARY_COLOR);
        footerPanel.setPreferredSize(new Dimension(800, 40));
        
        JLabel footerLabel = new JLabel("Admin Dashboard - Manage your heritage e-commerce platform", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void setupEventHandlers() {
        productManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductManagement();
            }
        });
        
        categoryManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCategoryManagement();
            }
        });
        
        userManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openUserManagement();
            }
        });
        
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOrderManagement();
            }
        });
        
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReports();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
    }
    
    private void openProductManagement() {
        new ProductManagementView().setVisible(true);
    }
    
    private void openCategoryManagement() {
        new CategoryManagementView().setVisible(true);
    }
    
    private void openUserManagement() {
        new UserManagementView().setVisible(true);
    }
    
    private void openOrderManagement() {
        new OrderManagementView().setVisible(true);
    }
    
    private void openReports() {
        // Show options for different types of reports
        String[] options = {"Standard Reports", "Advanced Analytics"};
        String choice = (String) JOptionPane.showInputDialog(this,
            "Select report type:", "Reports & Analytics",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            if ("Standard Reports".equals(choice)) {
                new ReportsDashboardView().setVisible(true);
            } else if ("Advanced Analytics".equals(choice)) {
                new AdvancedAnalyticsView().setVisible(true);
            }
        }
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
