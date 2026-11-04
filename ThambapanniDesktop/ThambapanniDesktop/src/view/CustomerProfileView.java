package view;

import controller.AuthController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Customer profile management view
 */
public class CustomerProfileView extends JFrame {
    
    private AuthController authController;
    private UserController userController;
    
    // UI Components
    private JTextField usernameField, emailField, fullNameField, phoneField, addressField;
    private JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;
    private JButton updateProfileButton, changePasswordButton, cancelButton;
    private JLabel statusLabel;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);
    private static final Color SUCCESS_COLOR = new Color(34, 139, 34);
    private static final Color ERROR_COLOR = new Color(220, 20, 60);
    
    public CustomerProfileView() {
        this.authController = new AuthController();
        this.userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserProfile();
    }
    
    public CustomerProfileView(AuthController authController) {
        this.authController = authController;
        this.userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserProfile();
    }
    
    private void initializeComponents() {
        setTitle("My Profile - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Initialize form fields
        usernameField = new JTextField(20);
        usernameField.setEditable(false); // Username cannot be changed
        emailField = new JTextField(20);
        fullNameField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        
        // Password fields
        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        
        // Buttons
        updateProfileButton = new JButton("Update Profile");
        changePasswordButton = new JButton("Change Password");
        cancelButton = new JButton("Cancel");
        
        // Style buttons
        updateProfileButton.setBackground(PRIMARY_COLOR);
        updateProfileButton.setForeground(Color.WHITE);
        updateProfileButton.setFocusPainted(false);
        
        changePasswordButton.setBackground(SECONDARY_COLOR);
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        // Profile information section
        JPanel profilePanel = createProfilePanel();
        mainPanel.add(profilePanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Password change section
        JPanel passwordPanel = createPasswordPanel();
        mainPanel.add(passwordPanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(fullNameField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);
        
        return panel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Current Password
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        panel.add(currentPasswordField, gbc);
        
        // New Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        panel.add(newPasswordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        panel.add(updateProfileButton);
        panel.add(changePasswordButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        updateProfileButton.addActionListener(e -> updateProfile());
        changePasswordButton.addActionListener(e -> changePassword());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadUserProfile() {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            fullNameField.setText(currentUser.getFullName());
            phoneField.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            addressField.setText(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        } else {
            showStatus("Error: User not logged in", ERROR_COLOR);
        }
    }
    
    private void updateProfile() {
        try {
            User currentUser = authController.getCurrentUser();
            if (currentUser == null) {
                showStatus("Error: User not logged in", ERROR_COLOR);
                return;
            }
            
            // Validate input
            if (!validateProfileInput()) {
                return;
            }
            
            // Update user object
            currentUser.setEmail(emailField.getText().trim());
            currentUser.setFullName(fullNameField.getText().trim());
            currentUser.setPhone(phoneField.getText().trim());
            currentUser.setAddress(addressField.getText().trim());
            
            // Update in database
            if (userController.updateUser(currentUser)) {
                showStatus("Profile updated successfully!", SUCCESS_COLOR);
            } else {
                showStatus("Failed to update profile. Email might already exist.", ERROR_COLOR);
            }
            
        } catch (Exception e) {
            showStatus("Error updating profile: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void changePassword() {
        try {
            User currentUser = authController.getCurrentUser();
            if (currentUser == null) {
                showStatus("Error: User not logged in", ERROR_COLOR);
                return;
            }
            
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validate passwords
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showStatus("Please fill in all password fields", ERROR_COLOR);
                return;
            }
            
            if (!currentPassword.equals(currentUser.getPassword())) {
                showStatus("Current password is incorrect", ERROR_COLOR);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                showStatus("New passwords do not match", ERROR_COLOR);
                return;
            }
            
            if (newPassword.length() < 6) {
                showStatus("New password must be at least 6 characters long", ERROR_COLOR);
                return;
            }
            
            // Update password
            if (userController.changeUserPassword(currentUser.getUserId(), newPassword)) {
                showStatus("Password changed successfully!", SUCCESS_COLOR);
                // Clear password fields
                currentPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            } else {
                showStatus("Failed to change password", ERROR_COLOR);
            }
            
        } catch (Exception e) {
            showStatus("Error changing password: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private boolean validateProfileInput() {
        if (emailField.getText().trim().isEmpty()) {
            showStatus("Email is required", ERROR_COLOR);
            return false;
        }
        
        if (fullNameField.getText().trim().isEmpty()) {
            showStatus("Full name is required", ERROR_COLOR);
            return false;
        }
        
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showStatus("Please enter a valid email address", ERROR_COLOR);
            return false;
        }
        
        return true;
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Clear status after 5 seconds
        Timer timer = new Timer(5000, e -> statusLabel.setText(" "));
        timer.setRepeats(false);
        timer.start();
    }
}
