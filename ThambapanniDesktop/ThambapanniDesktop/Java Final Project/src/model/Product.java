package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Product model class representing Sri Lankan heritage products
 */
public class Product {
    
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private int categoryId;
    private String imageUrl;
    private String origin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Category name for display purposes (not stored in database)
    private String categoryName;
    
    // Default constructor
    public Product() {
        this.price = BigDecimal.ZERO;
        this.stockQuantity = 0;
    }
    
    // Constructor with essential fields
    public Product(String productName, String description, BigDecimal price, 
                  int stockQuantity, int categoryId) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
    }
    
    // Constructor with all fields
    public Product(int productId, String productName, String description, 
                  BigDecimal price, int stockQuantity, int categoryId, 
                  String imageUrl, String origin, Timestamp createdAt, 
                  Timestamp updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.origin = origin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    // Utility methods
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    public boolean isLowStock() {
        return stockQuantity > 0 && stockQuantity <= 10;
    }
    
    public boolean isOutOfStock() {
        return stockQuantity <= 0;
    }
    
    public String getFormattedPrice() {
        return "LKR " + price.toString();
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", categoryName='" + categoryName + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId == product.productId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}
