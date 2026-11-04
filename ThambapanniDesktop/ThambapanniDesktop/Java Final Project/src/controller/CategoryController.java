package controller;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

/**
 * Controller for category management operations
 */
public class CategoryController {
    
    private CategoryDAO categoryDAO;
    
    public CategoryController() {
        this.categoryDAO = new CategoryDAO();
    }
    
    /**
     * Create a new category
     * @param category Category to create
     * @return true if successful, false otherwise
     */
    public boolean createCategory(Category category) {
        try {
            // Validate category name doesn't already exist
            if (categoryDAO.categoryNameExists(category.getCategoryName())) {
                return false;
            }
            return categoryDAO.createCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object or null if not found
     */
    public Category getCategoryById(int categoryId) {
        try {
            return categoryDAO.getCategoryById(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all categories
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        try {
            return categoryDAO.getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
    
    /**
     * Update category information
     * @param category Category with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        try {
            // Check if category exists
            Category existingCategory = categoryDAO.getCategoryById(category.getCategoryId());
            if (existingCategory == null) {
                return false;
            }
            
            // Check if new name conflicts with existing category (excluding current one)
            if (!existingCategory.getCategoryName().equals(category.getCategoryName()) &&
                categoryDAO.categoryNameExists(category.getCategoryName())) {
                return false;
            }
            
            return categoryDAO.updateCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete category by ID
     * @param categoryId Category ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        try {
            // Check if category has products
            int productCount = categoryDAO.getProductCountByCategory(categoryId);
            if (productCount > 0) {
                return false; // Cannot delete category with products
            }
            
            return categoryDAO.deleteCategory(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if category name exists
     * @param categoryName Category name to check
     * @return true if exists, false otherwise
     */
    public boolean categoryNameExists(String categoryName) {
        try {
            return categoryDAO.categoryNameExists(categoryName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get product count for a category
     * @param categoryId Category ID
     * @return Number of products in the category
     */
    public int getProductCountByCategory(int categoryId) {
        try {
            return categoryDAO.getProductCountByCategory(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Check if category can be deleted (has no products)
     * @param categoryId Category ID
     * @return true if can be deleted, false otherwise
     */
    public boolean canDeleteCategory(int categoryId) {
        try {
            return categoryDAO.getProductCountByCategory(categoryId) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
