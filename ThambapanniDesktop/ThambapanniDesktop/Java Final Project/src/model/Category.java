package model;

import java.sql.Timestamp;

/**
 * Category model class representing product categories
 */
public class Category {
    
    private int categoryId;
    private String categoryName;
    private String description;
    private Timestamp createdAt;
    
    // Default constructor
    public Category() {
    }
    
    // Constructor with essential fields
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
    
    // Constructor with all fields
    public Category(int categoryId, String categoryName, String description, Timestamp createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return categoryName; // For ComboBox display
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return categoryId == category.categoryId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(categoryId);
    }
}
