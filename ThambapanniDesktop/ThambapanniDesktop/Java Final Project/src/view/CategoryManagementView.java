package view;

import controller.CategoryController;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Category management view for admin operations
 */
public class CategoryManagementView extends JFrame {

    private CategoryController categoryController;

    // UI Components
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(139, 69, 19);
    private static final Color SECONDARY_COLOR = new Color(205, 133, 63);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 220);

    public CategoryManagementView() {
        categoryController = new CategoryController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCategories();
    }

    private void initializeComponents() {
        setTitle("Category Management - Thambapanni Sobha");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize table
        String[] columnNames = {"ID", "Category Name", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCategory();
            }
        });

        // Initialize form fields
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        // Initialize buttons
        addButton = new JButton("Add Category");
        updateButton = new JButton("Update Category");
        deleteButton = new JButton("Delete Category");
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

        JLabel titleLabel = new JLabel("Category Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Categories"));

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));
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
        formPanel.setBorder(BorderFactory.createTitledBorder("Category Details"));
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        // Category name
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Category Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

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
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        refreshButton.addActionListener(e -> {
            loadCategories();
            clearForm();
        });
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Category> categories = categoryController.getAllCategories();

        for (Category category : categories) {
            Object[] row = {
                category.getCategoryId(),
                category.getCategoryName(),
                category.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Category category = categoryController.getCategoryById(categoryId);

            if (category != null) {
                nameField.setText(category.getCategoryName());
                descriptionArea.setText(category.getDescription());
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        } else {
            clearForm();
        }
    }

    private void addCategory() {
        if (validateForm()) {
            Category category = new Category();
            category.setCategoryName(nameField.getText().trim());
            category.setDescription(descriptionArea.getText().trim());

            if (categoryController.createCategory(category)) {
                JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCategories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add category. Category name might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0 && validateForm()) {
            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Category category = new Category();
            category.setCategoryId(categoryId);
            category.setCategoryName(nameField.getText().trim());
            category.setDescription(descriptionArea.getText().trim());

            if (categoryController.updateCategory(category)) {
                JOptionPane.showMessageDialog(this, "Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCategories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String categoryName = (String) tableModel.getValueAt(selectedRow, 1);

            // Check if category has products
            int productCount = categoryController.getProductCountByCategory(categoryId);
            if (productCount > 0) {
                JOptionPane.showMessageDialog(this,
                    "Cannot delete category '" + categoryName + "' because it has " + productCount + " products.",
                    "Cannot Delete", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the category '" + categoryName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (categoryController.deleteCategory(categoryId)) {
                    JOptionPane.showMessageDialog(this, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategories();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }

        return true;
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        categoryTable.clearSelection();
    }
}
