package view;

import controller.AuthController;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Customer profile management view
 */
public class CustomerProfileView extends JFrame {

    private AuthController authController;

    // UI Components
    private JTextField emailField, fullNameField, phoneField;
    private JTextArea addressArea;
    private JButton updateButton, changePasswordButton, cancelButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public CustomerProfileView() {
        authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUserProfile();
    }

    private void initializeComponents() {
        setTitle("My Profile - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize form fields
        emailField = new JTextField(20);
        fullNameField = new JTextField(20);
        phoneField = new JTextField(15);
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        // Initialize buttons
        updateButton = new JButton("Update Profile");
        changePasswordButton = new JButton("Change Password");
        cancelButton = new JButton("Cancel");

        styleButton(updateButton);
        styleButton(changePasswordButton);
        styleButton(cancelButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username (read-only)
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            formPanel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField usernameField = new JTextField(currentUser.getUsername());
            usernameField.setEditable(false);
            usernameField.setBackground(Color.LIGHT_GRAY);
            formPanel.add(usernameField, gbc);
        }

        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(fullNameField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        formPanel.add(addressScrollPane, gbc);

        // Button panel
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(updateButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        updateButton.addActionListener(e -> updateProfile());
        changePasswordButton.addActionListener(e -> changePassword());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadUserProfile() {
        User currentUser = authController.getCurrentUser();
        if (currentUser != null) {
            emailField.setText(currentUser.getEmail());
            fullNameField.setText(currentUser.getFullName());
            phoneField.setText(currentUser.getPhone());
            addressArea.setText(currentUser.getAddress());
        }
    }

    private void updateProfile() {
        if (validateForm()) {
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();

            if (authController.updateCurrentUserProfile(email, fullName, phone, address)) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile. Email might already be in use.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        panel.add(new JLabel("Current Password:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all password fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "New password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authController.changePassword(currentPassword, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to change password. Current password might be incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateForm() {
        if (emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            fullNameField.requestFocus();
            return false;
        }

        return true;
    }
}
