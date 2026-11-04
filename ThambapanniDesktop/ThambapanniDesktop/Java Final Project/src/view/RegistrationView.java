package view;

import controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration view for new users
 */
public class RegistrationView extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JButton registerButton;
    private JButton backToLoginButton;
    private AuthController authController;
    
    // Color scheme (same as LoginView)
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color ACCENT_COLOR = new Color(255, 215, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color TEXT_COLOR = new Color(101, 67, 33);
    
    public RegistrationView() {
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Register - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(650, 550));

        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create components with larger column count
        usernameField = new JTextField(25);
        passwordField = new JPasswordField(25);
        confirmPasswordField = new JPasswordField(25);
        emailField = new JTextField(25);
        fullNameField = new JTextField(25);
        phoneField = new JTextField(25);
        addressArea = new JTextArea(4, 25);
        registerButton = new JButton("Register");
        backToLoginButton = new JButton("Back to Login");

        // Style components
        styleTextField(usernameField);
        styleTextField(passwordField);
        styleTextField(confirmPasswordField);
        styleTextField(emailField);
        styleTextField(fullNameField);
        styleTextField(phoneField);
        styleTextArea(addressArea);
        styleButton(registerButton, PRIMARY_COLOR);
        styleButton(backToLoginButton, SECONDARY_COLOR);
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
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(600, 60));
        
        JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel);
        return headerPanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Form fields
        addFormField(mainPanel, "Username:", usernameField, 0, gbc);
        addFormField(mainPanel, "Password:", passwordField, 1, gbc);
        addFormField(mainPanel, "Confirm Password:", confirmPasswordField, 2, gbc);
        addFormField(mainPanel, "Email:", emailField, 3, gbc);
        addFormField(mainPanel, "Full Name:", fullNameField, 4, gbc);
        addFormField(mainPanel, "Phone:", phoneField, 5, gbc);
        
        // Address field (special handling for text area)
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        addressLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        mainPanel.add(addressLabel, gbc);
        
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        addressScrollPane.setPreferredSize(new Dimension(300, 80));
        addressScrollPane.setMinimumSize(new Dimension(250, 70));
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 5, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(addressScrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(registerButton);
        buttonPanel.add(backToLoginButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        return mainPanel;
    }
    
    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.insets = new Insets(8, 20, 8, 15);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.insets = new Insets(8, 15, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(SECONDARY_COLOR);
        footerPanel.setPreferredSize(new Dimension(600, 30));
        
        JLabel footerLabel = new JLabel("Join our community of heritage enthusiasts", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        
        footerPanel.add(footerLabel);
        return footerPanel;
    }
    
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setPreferredSize(new Dimension(300, 40));
        textField.setMinimumSize(new Dimension(250, 35));
    }
    
    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(TEXT_COLOR);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupEventHandlers() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
        
        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToLogin();
            }
        });
        
        getRootPane().setDefaultButton(registerButton);
    }
    
    private void handleRegistration() {
        // Get form data
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        
        // Validate form
        if (!validateForm(username, password, confirmPassword, email, fullName)) {
            return;
        }
        
        // Register user
        if (authController.registerUser(username, password, email, fullName, phone, address)) {
            showSuccessMessage("Registration successful! You can now login with your credentials.");
            backToLogin();
        } else {
            showErrorMessage("Registration failed. Username or email might already exist.");
        }
    }
    
    private boolean validateForm(String username, String password, String confirmPassword, String email, String fullName) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            showErrorMessage("Please fill in all required fields (Username, Password, Email, Full Name).");
            return false;
        }
        
        if (username.length() < 3) {
            showErrorMessage("Username must be at least 3 characters long.");
            return false;
        }
        
        if (password.length() < 6) {
            showErrorMessage("Password must be at least 6 characters long.");
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match.");
            return false;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showErrorMessage("Please enter a valid email address.");
            return false;
        }
        
        return true;
    }
    
    private void backToLogin() {
        new LoginView().setVisible(true);
        dispose();
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
