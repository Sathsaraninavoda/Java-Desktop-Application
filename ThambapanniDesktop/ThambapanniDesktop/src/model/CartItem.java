package model;

import java.math.BigDecimal;

/**
 * CartItem model class representing items in a shopping cart
 */
public class CartItem {
    
    private int productId;
    private String productName;
    private String productDescription;
    private BigDecimal unitPrice;
    private int quantity;
    private String imageUrl;
    private int stockQuantity;
    
    // Default constructor
    public CartItem() {
        this.quantity = 1;
        this.unitPrice = BigDecimal.ZERO;
    }
    
    // Constructor with essential fields
    public CartItem(int productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
    
    // Constructor from Product
    public CartItem(Product product, int quantity) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productDescription = product.getDescription();
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
        this.imageUrl = product.getImageUrl();
        this.stockQuantity = product.getStockQuantity();
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
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    // Utility methods
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public String getFormattedUnitPrice() {
        return "LKR " + String.format("%.2f", unitPrice);
    }
    
    public String getFormattedTotalPrice() {
        return "LKR " + String.format("%.2f", getTotalPrice());
    }
    
    public boolean isInStock() {
        return stockQuantity >= quantity;
    }
    
    public boolean canIncreaseQuantity() {
        return quantity < stockQuantity;
    }
    
    public void increaseQuantity() {
        if (canIncreaseQuantity()) {
            quantity++;
        }
    }
    
    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
        }
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return productId == cartItem.productId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}
