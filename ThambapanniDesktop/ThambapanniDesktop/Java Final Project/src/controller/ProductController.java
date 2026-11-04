package controller;

import dao.ProductDAO;
import model.Product;

import java.util.List;

/**
 * Controller for product management operations
 */
public class ProductController {
    
    private ProductDAO productDAO;
    
    public ProductController() {
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Create a new product
     * @param product Product to create
     * @return true if successful, false otherwise
     */
    public boolean createProduct(Product product) {
        try {
            return productDAO.createProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get product by ID
     * @param productId Product ID
     * @return Product object or null if not found
     */
    public Product getProductById(int productId) {
        try {
            return productDAO.getProductById(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all products
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
    
    /**
     * Get products by category
     * @param categoryId Category ID
     * @return List of products in the category
     */
    public List<Product> getProductsByCategory(int categoryId) {
        try {
            return productDAO.getProductsByCategory(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Search products by name
     * @param searchTerm Search term
     * @return List of matching products
     */
    public List<Product> searchProducts(String searchTerm) {
        try {
            return productDAO.searchProducts(searchTerm);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Update product
     * @param product Product with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        try {
            return productDAO.updateProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete product
     * @param productId Product ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        try {
            return productDAO.deleteProduct(productId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update product stock
     * @param productId Product ID
     * @param newQuantity New stock quantity
     * @return true if successful, false otherwise
     */
    public boolean updateStock(int productId, int newQuantity) {
        try {
            return productDAO.updateStock(productId, newQuantity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get low stock products
     * @return List of products with low stock
     */
    public List<Product> getLowStockProducts() {
        try {
            return productDAO.getLowStockProducts();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Validate product data
     * @param product Product to validate
     * @return Error message if validation fails, null if valid
     */
    public String validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            return "Product name is required.";
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return "Price must be greater than 0.";
        }
        
        if (product.getStockQuantity() < 0) {
            return "Stock quantity cannot be negative.";
        }
        
        if (product.getCategoryId() <= 0) {
            return "Please select a valid category.";
        }
        
        return null; // Valid product
    }
}
