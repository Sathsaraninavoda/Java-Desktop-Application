package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User management view for admin operations
 */
public class UserManagementView extends JFrame {

    private UserController userController;

    // UI Components
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField, emailField, fullNameField, phoneField;
    private JTextArea addressArea;
    private JComboBox<User.UserType> userTypeCombo;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public UserManagementView() {
        userController = new UserController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadUsers();
    }

    private void initializeComponents() {
        setTitle("User Management - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize table
        String[] columnNames = {"ID", "Username", "Email", "Full Name", "Phone", "User Type", "Created"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedUser();
            }
        });

        // Initialize form fields
        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        fullNameField = new JTextField(20);
        phoneField = new JTextField(15);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        userTypeCombo = new JComboBox<>(User.UserType.values());

        // Initialize buttons
        addButton = new JButton("Add User");
        updateButton = new JButton("Update User");
        deleteButton = new JButton("Delete User");
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

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Users"));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel tableButtonPanel = new JPanel(new FlowLayout());
        tableButtonPanel.add(refreshButton);
        tablePanel.add(tableButtonPanel, BorderLayout.SOUTH);

        // Form panel
        JPanel formPanel = createFormPanel();

        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1: Username and Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Row 2: Full Name and Phone
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(phoneField, gbc);

        // Row 3: User Type
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(userTypeCombo, gbc);

        // Row 4: Address
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        formPanel.add(addressScrollPane, gbc);

        // Row 5: Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        refreshButton.addActionListener(e -> {
            loadUsers();
            clearForm();
        });
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();

        for (User user : users) {
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhone(),
                user.getUserType(),
                user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 10) : ""
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            User user = userController.getUserById(userId);

            if (user != null) {
                usernameField.setText(user.getUsername());
                emailField.setText(user.getEmail());
                fullNameField.setText(user.getFullName());
                phoneField.setText(user.getPhone());
                addressArea.setText(user.getAddress());
                userTypeCombo.setSelectedItem(user.getUserType());

                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);

                // Disable username editing for existing users
                usernameField.setEnabled(false);
            }
        } else {
            clearForm();
        }
    }

    private void addUser() {
        if (validateForm()) {
            // Get password from user
            String password = JOptionPane.showInputDialog(this,
                "Enter password for new user:",
                "New User Password",
                JOptionPane.QUESTION_MESSAGE);

            if (password != null && !password.trim().isEmpty()) {
                User user = new User();
                user.setUsername(usernameField.getText().trim());
                user.setPassword(password.trim());
                user.setEmail(emailField.getText().trim());
                user.setFullName(fullNameField.getText().trim());
                user.setPhone(phoneField.getText().trim());
                user.setAddress(addressArea.getText().trim());
                user.setUserType((User.UserType) userTypeCombo.getSelectedItem());

                if (userController.createUser(user)) {
                    JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add user. Username or email might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0 && validateForm()) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            User user = userController.getUserById(userId);

            if (user != null) {
                // Update user fields (keep existing password)
                user.setEmail(emailField.getText().trim());
                user.setFullName(fullNameField.getText().trim());
                user.setPhone(phoneField.getText().trim());
                user.setAddress(addressArea.getText().trim());
                user.setUserType((User.UserType) userTypeCombo.getSelectedItem());

                if (userController.updateUser(user)) {
                    JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);

            // Prevent deleting admin user
            if ("admin".equals(username)) {
                JOptionPane.showMessageDialog(this, "Cannot delete the admin user.", "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user '" + username + "'?\nThis will also delete all their orders.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (userController.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateForm() {
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }

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

    private void clearForm() {
        usernameField.setText("");
        emailField.setText("");
        fullNameField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        userTypeCombo.setSelectedItem(User.UserType.CUSTOMER);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        userTable.clearSelection();
        usernameField.setEnabled(true); // Re-enable for new user
    }
}
