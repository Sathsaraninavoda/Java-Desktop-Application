package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * SalesReport model class for sales analytics
 */
public class SalesReport {
    
    private Date reportDate;
    private int totalOrders;
    private BigDecimal totalSales;
    private BigDecimal averageOrderValue;
    private int totalCustomers;
    private int newCustomers;
    private String period; // DAILY, WEEKLY, MONTHLY, YEARLY
    
    // Default constructor
    public SalesReport() {
        this.totalSales = BigDecimal.ZERO;
        this.averageOrderValue = BigDecimal.ZERO;
    }
    
    // Constructor with essential fields
    public SalesReport(Date reportDate, int totalOrders, BigDecimal totalSales, String period) {
        this.reportDate = reportDate;
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
        this.period = period;
        this.averageOrderValue = totalOrders > 0 ? totalSales.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public Date getReportDate() {
        return reportDate;
    }
    
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
        calculateAverageOrderValue();
    }
    
    public BigDecimal getTotalSales() {
        return totalSales;
    }
    
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
        calculateAverageOrderValue();
    }
    
    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }
    
    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }
    
    public int getTotalCustomers() {
        return totalCustomers;
    }
    
    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
    
    public int getNewCustomers() {
        return newCustomers;
    }
    
    public void setNewCustomers(int newCustomers) {
        this.newCustomers = newCustomers;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    // Utility methods
    private void calculateAverageOrderValue() {
        if (totalOrders > 0 && totalSales != null) {
            this.averageOrderValue = totalSales.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            this.averageOrderValue = BigDecimal.ZERO;
        }
    }
    
    public String getFormattedTotalSales() {
        return "LKR " + String.format("%.2f", totalSales);
    }
    
    public String getFormattedAverageOrderValue() {
        return "LKR " + String.format("%.2f", averageOrderValue);
    }
    
    @Override
    public String toString() {
        return "SalesReport{" +
                "reportDate=" + reportDate +
                ", totalOrders=" + totalOrders +
                ", totalSales=" + totalSales +
                ", averageOrderValue=" + averageOrderValue +
                ", period='" + period + '\'' +
                '}';
    }
}

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
