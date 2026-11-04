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
