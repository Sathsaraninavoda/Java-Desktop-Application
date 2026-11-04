package model;

import java.math.BigDecimal;

/**
 * OrderItem model class representing individual items in an order
 */
public class OrderItem {
    
    private int itemId;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    
    // Additional fields for display purposes
    private String productName;
    private String productDescription;
    
    // Default constructor
    public OrderItem() {
        this.quantity = 1;
        this.unitPrice = BigDecimal.ZERO;
    }
    
    // Constructor with essential fields
    public OrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Constructor with all fields
    public OrderItem(int itemId, int orderId, int productId, int quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getters and Setters
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
    
    // Utility methods
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public String getFormattedUnitPrice() {
        return "LKR " + unitPrice.toString();
    }
    
    public String getFormattedTotalPrice() {
        return "LKR " + getTotalPrice().toString();
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "itemId=" + itemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderItem orderItem = (OrderItem) obj;
        return itemId == orderItem.itemId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(itemId);
    }
}
