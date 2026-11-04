package model;

import java.math.BigDecimal;

/**
 * InventoryReport model class for inventory analytics
 */
public class InventoryReport {
    
    private int productId;
    private String productName;
    private String categoryName;
    private int currentStock;
    private BigDecimal unitPrice;
    private BigDecimal inventoryValue;
    private String stockStatus; // LOW, MEDIUM, GOOD, OUT_OF_STOCK
    private int reorderLevel;
    
    // Default constructor
    public InventoryReport() {
        this.unitPrice = BigDecimal.ZERO;
        this.inventoryValue = BigDecimal.ZERO;
        this.reorderLevel = 10; // Default reorder level
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
    
    public int getCurrentStock() {
        return currentStock;
    }
    
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
        calculateInventoryValue();
        updateStockStatus();
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateInventoryValue();
    }
    
    public BigDecimal getInventoryValue() {
        return inventoryValue;
    }
    
    public void setInventoryValue(BigDecimal inventoryValue) {
        this.inventoryValue = inventoryValue;
    }
    
    public String getStockStatus() {
        return stockStatus;
    }
    
    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }
    
    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
        updateStockStatus();
    }
    
    // Utility methods
    private void calculateInventoryValue() {
        if (unitPrice != null) {
            this.inventoryValue = unitPrice.multiply(BigDecimal.valueOf(currentStock));
        }
    }
    
    private void updateStockStatus() {
        if (currentStock == 0) {
            this.stockStatus = "OUT_OF_STOCK";
        } else if (currentStock <= reorderLevel) {
            this.stockStatus = "LOW";
        } else if (currentStock <= reorderLevel * 2) {
            this.stockStatus = "MEDIUM";
        } else {
            this.stockStatus = "GOOD";
        }
    }
    
    public String getFormattedUnitPrice() {
        return "LKR " + String.format("%.2f", unitPrice);
    }
    
    public String getFormattedInventoryValue() {
        return "LKR " + String.format("%.2f", inventoryValue);
    }
    
    public boolean needsReorder() {
        return currentStock <= reorderLevel;
    }
    
    @Override
    public String toString() {
        return "InventoryReport{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", currentStock=" + currentStock +
                ", stockStatus='" + stockStatus + '\'' +
                ", inventoryValue=" + inventoryValue +
                '}';
    }
}
