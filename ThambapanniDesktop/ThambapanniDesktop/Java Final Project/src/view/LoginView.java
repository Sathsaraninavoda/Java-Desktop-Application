package view;

import controller.AuthController;
import database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login view for Thambapanni Sobha application
 */
public class LoginView extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private AuthController authController;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19); // Saddle Brown
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63); // Peru
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); // Gold
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220); // Beige
    private static final Color TEXT_COLOR = new Color(101, 67, 33); // Dark Brown
    
    public LoginView() {
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();

        // Initialize database on startup (gracefully handle connection failures)
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            System.out.println("Note: Database connection failed. Application will run in demo mode.");
        }
    }
    
    private void initializeComponents() {
        setTitle("Thambapanni Sobha - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set background color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create components
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("LOGIN");
        registerButton = new JButton("CREATE NEW ACCOUNT");

        // Style components
        styleTextField(usernameField);
        styleTextField(passwordField);
        styleButton(loginButton, PRIMARY_COLOR);
        styleButton(registerButton, SECONDARY_COLOR);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create main container with side-by-side layout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(BACKGROUND_COLOR);

        // Left side - Welcome/Branding panel
        JPanel leftPanel = createWelcomePanel();
        containerPanel.add(leftPanel, BorderLayout.WEST);

        // Right side - Login form panel
        JPanel rightPanel = createLoginFormPanel();
        containerPanel.add(rightPanel, BorderLayout.CENTER);

        add(containerPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(PRIMARY_COLOR);
        welcomePanel.setPreferredSize(new Dimension(400, 600));
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(PRIMARY_COLOR);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Logo section
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(PRIMARY_COLOR);
        logoPanel.setMaximumSize(new Dimension(320, 120));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel logoLabel = new JLabel("THAMBAPANNI", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Serif", Font.BOLD, 24));
        logoLabel.setForeground(ACCENT_COLOR);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoSubLabel = new JLabel("SOBHA", SwingConstants.CENTER);
        logoSubLabel.setFont(new Font("Serif", Font.BOLD, 20));
        logoSubLabel.setForeground(Color.WHITE);
        logoSubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(logoSubLabel);

        // Welcome text
        JLabel welcomeTitle = new JLabel("Welcome to");
        welcomeTitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        welcomeTitle.setForeground(Color.WHITE);
        welcomeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel("Sri Lankan Heritage");
        mainTitle.setFont(new Font("Serif", Font.BOLD, 28));
        mainTitle.setForeground(ACCENT_COLOR);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("E-commerce Platform");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subTitle.setForeground(Color.WHITE);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Description
        JTextArea descriptionArea = new JTextArea(
            "Discover authentic Sri Lankan products including traditional spices, " +
            "premium Ceylon tea, Ayurvedic wellness products, handcrafted items, " +
            "and cultural treasures from the pearl of the Indian Ocean."
        );
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setForeground(new Color(255, 255, 255, 200));
        descriptionArea.setBackground(PRIMARY_COLOR);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        descriptionArea.setMaximumSize(new Dimension(320, 120));

        // Add components with spacing
        contentPanel.add(logoPanel);
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(welcomeTitle);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(mainTitle);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(subTitle);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(descriptionArea);

        welcomePanel.add(contentPanel, BorderLayout.CENTER);

        return welcomePanel;
    }
    
    private JPanel createLoginFormPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 600));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // Form container
        JPanel formContainer = new JPanel();
        formContainer.setBackground(Color.WHITE);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));

        // Login title
        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        loginTitle.setForeground(PRIMARY_COLOR);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginSubtitle = new JLabel("Access your account");
        loginSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loginSubtitle.setForeground(new Color(100, 100, 100));
        loginSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username section
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(300, 50));

        // Password section
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 50));

        // Buttons
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(300, 50));

        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(300, 50));

        // Divider
        JLabel dividerLabel = new JLabel("Don't have an account?");
        dividerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dividerLabel.setForeground(new Color(100, 100, 100));
        dividerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components with proper spacing
        formContainer.add(loginTitle);
        formContainer.add(Box.createVerticalStrut(5));
        formContainer.add(loginSubtitle);
        formContainer.add(Box.createVerticalStrut(40));

        formContainer.add(usernameLabel);
        formContainer.add(Box.createVerticalStrut(8));
        formContainer.add(usernameField);
        formContainer.add(Box.createVerticalStrut(25));

        formContainer.add(passwordLabel);
        formContainer.add(Box.createVerticalStrut(8));
        formContainer.add(passwordField);
        formContainer.add(Box.createVerticalStrut(35));

        formContainer.add(loginButton);
        formContainer.add(Box.createVerticalStrut(30));

        formContainer.add(dividerLabel);
        formContainer.add(Box.createVerticalStrut(15));
        formContainer.add(registerButton);
        formContainer.add(Box.createVerticalStrut(30));

        // Credentials info
        JPanel credentialsPanel = createCredentialsInfoPanel();
        credentialsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(credentialsPanel);

        rightPanel.add(formContainer, BorderLayout.NORTH);

        return rightPanel;
    }
    
    private JPanel createCredentialsInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(248, 249, 250)); // Very light gray
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setMaximumSize(new Dimension(300, 120));

        JLabel titleLabel = new JLabel("Demo Credentials:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminLabel = new JLabel("Admin: admin / admin123");
        adminLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        adminLabel.setForeground(TEXT_COLOR);
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel customerLabel = new JLabel("Customer: customer1 / password123");
        customerLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        customerLabel.setForeground(TEXT_COLOR);
        customerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(adminLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(customerLabel);

        return infoPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(50, 50, 50));
        footerPanel.setPreferredSize(new Dimension(800, 35));

        JLabel footerLabel = new JLabel("© 2025 Thambapanni Sobha - Promoting Sri Lankan Heritage Worldwide", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(200, 200, 200));

        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setPreferredSize(new Dimension(300, 50));

        // Add focus effects
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setPreferredSize(new Dimension(300, 50));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        // Add hover and press effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
        });
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationView();
            }
        });
        
        // Enter key support
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter both username and password.");
            return;
        }
        
        // Authenticate user
        if (authController.authenticateUser(username, password)) {
            dispose(); // Close login window
            
            // Open appropriate dashboard based on user type
            if (authController.getCurrentUser().isAdmin()) {
                new AdminDashboardView().setVisible(true);
            } else {
                new CustomerDashboardView().setVisible(true);
            }
        } else {
            showErrorMessage("Invalid username or password.");
            passwordField.setText(""); // Clear password field
        }
    }
    
    private void openRegistrationView() {
        new RegistrationView().setVisible(true);
        dispose(); // Close login window
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Utility method to show success message
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
