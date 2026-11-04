package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order model class representing customer orders
 */
public class Order {
    
    public enum OrderStatus {
        PENDING, PROCESSING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }
    
    private int orderId;
    private int userId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private String shippingAddress;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for display purposes
    private String customerName;
    private String customerEmail;
    private List<OrderItem> orderItems;
    
    // Default constructor
    public Order() {
        this.totalAmount = BigDecimal.ZERO;
        this.orderStatus = OrderStatus.PENDING;
    }
    
    // Constructor with essential fields
    public Order(int userId, BigDecimal totalAmount, String shippingAddress) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.orderStatus = OrderStatus.PENDING;
    }
    
    // Constructor with all fields
    public Order(int orderId, int userId, BigDecimal totalAmount, OrderStatus orderStatus,
                String shippingAddress, Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
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
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    // Utility methods
    public String getFormattedTotal() {
        return "LKR " + totalAmount.toString();
    }
    
    public boolean isPending() {
        return orderStatus == OrderStatus.PENDING;
    }
    
    public boolean isConfirmed() {
        return orderStatus == OrderStatus.CONFIRMED;
    }
    
    public boolean isShipped() {
        return orderStatus == OrderStatus.SHIPPED;
    }
    
    public boolean isDelivered() {
        return orderStatus == OrderStatus.DELIVERED;
    }
    
    public boolean isCancelled() {
        return orderStatus == OrderStatus.CANCELLED;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", orderStatus=" + orderStatus +
                ", customerName='" + customerName + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId == order.orderId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}
