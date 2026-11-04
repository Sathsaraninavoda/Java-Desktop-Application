package model;

import java.math.BigDecimal;

/**
 * ProductSalesReport model class for product-specific sales analytics
 */
public class ProductSalesReport {
    
    private int productId;
    private String productName;
    private String categoryName;
    private int quantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal averagePrice;
    private int totalOrders;
    
    // Default constructor
    public ProductSalesReport() {
        this.totalRevenue = BigDecimal.ZERO;
        this.averagePrice = BigDecimal.ZERO;
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
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public int getQuantitySold() {
        return quantitySold;
    }
    
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public BigDecimal getAveragePrice() {
        return averagePrice;
    }
    
    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    // Utility methods
    public String getFormattedTotalRevenue() {
        return "LKR " + String.format("%.2f", totalRevenue);
    }
    
    public String getFormattedAveragePrice() {
        return "LKR " + String.format("%.2f", averagePrice);
    }
    
    @Override
    public String toString() {
        return "ProductSalesReport{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantitySold=" + quantitySold +
                ", totalRevenue=" + totalRevenue +
                '}';
    }
}
