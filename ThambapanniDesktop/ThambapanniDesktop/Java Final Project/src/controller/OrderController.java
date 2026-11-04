package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import model.Order;
import model.Order.OrderStatus;
import model.OrderItem;
import model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller for order management operations
 */
public class OrderController {
    
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private ProductDAO productDAO;
    
    public OrderController() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Create a new order with order items and inventory management
     * @param order Order to create
     * @param orderItems List of order items
     * @return true if successful, false otherwise
     */
    public boolean createOrder(Order order, List<OrderItem> orderItems) {
        try {
            // First, validate stock availability for all items
            for (OrderItem item : orderItems) {
                Product product = productDAO.getProductById(item.getProductId());
                if (product == null || product.getStockQuantity() < item.getQuantity()) {
                    return false; // Insufficient stock
                }
            }

            // Create the order first
            if (orderDAO.createOrder(order)) {
                // Create order items and update inventory
                boolean allItemsCreated = true;
                for (OrderItem item : orderItems) {
                    item.setOrderId(order.getOrderId());

                    // Create order item
                    if (orderItemDAO.createOrderItem(item)) {
                        // Update product inventory
                        Product product = productDAO.getProductById(item.getProductId());
                        if (product != null) {
                            int newStock = product.getStockQuantity() - item.getQuantity();
                            product.setStockQuantity(newStock);
                            productDAO.updateProduct(product);
                        }
                    } else {
                        allItemsCreated = false;
                        break;
                    }
                }

                if (!allItemsCreated) {
                    // If any item failed, we should ideally rollback the order
                    // For now, we'll return false
                    return false;
                }

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Create a simple order without items
     * @param order Order to create
     * @return true if successful, false otherwise
     */
    public boolean createOrder(Order order) {
        try {
            return orderDAO.createOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get order by ID
     * @param orderId Order ID
     * @return Order object or null if not found
     */
    public Order getOrderById(int orderId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) {
                // Load order items
                List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);
                order.setOrderItems(items);
            }
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all orders
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        try {
            return orderDAO.getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
    
    /**
     * Get orders by user ID
     * @param userId User ID
     * @return List of orders for the user
     */
    public List<Order> getOrdersByUserId(int userId) {
        try {
            return orderDAO.getOrdersByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Get orders by status
     * @param status Order status
     * @return List of orders with the specified status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        try {
            return orderDAO.getOrdersByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Update order information
     * @param order Order with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateOrder(Order order) {
        try {
            return orderDAO.updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update order status
     * @param orderId Order ID
     * @param newStatus New order status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, OrderStatus newStatus) {
        try {
            return orderDAO.updateOrderStatus(orderId, newStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cancel order
     * @param orderId Order ID
     * @return true if successful, false otherwise
     */
    public boolean cancelOrder(int orderId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order != null && order.getOrderStatus() == OrderStatus.PENDING) {
                return orderDAO.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            }
            return false; // Can only cancel pending orders
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete order (admin only)
     * @param orderId Order ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrder(int orderId) {
        try {
            return orderDAO.deleteOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get order items for an order
     * @param orderId Order ID
     * @return List of order items
     */
    public List<OrderItem> getOrderItems(int orderId) {
        try {
            return orderItemDAO.getOrderItemsByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Add item to order
     * @param orderItem Order item to add
     * @return true if successful, false otherwise
     */
    public boolean addOrderItem(OrderItem orderItem) {
        try {
            return orderItemDAO.createOrderItem(orderItem);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update order item
     * @param orderItem Order item with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateOrderItem(OrderItem orderItem) {
        try {
            return orderItemDAO.updateOrderItem(orderItem);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove item from order
     * @param itemId Order item ID
     * @return true if successful, false otherwise
     */
    public boolean removeOrderItem(int itemId) {
        try {
            return orderItemDAO.deleteOrderItem(itemId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Calculate order total from items
     * @param orderItems List of order items
     * @return Total amount
     */
    public BigDecimal calculateOrderTotal(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }
    
    /**
     * Get order statistics
     * @return Array with [totalOrders, pendingOrders, completedOrders, totalRevenue]
     */
    public Object[] getOrderStatistics() {
        try {
            return orderDAO.getOrderStatistics();
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[]{0, 0, 0, BigDecimal.ZERO};
        }
    }
    
    /**
     * Get best selling products
     * @param limit Number of products to return
     * @return List of best selling order items
     */
    public List<OrderItem> getBestSellingProducts(int limit) {
        try {
            return orderItemDAO.getBestSellingProducts(limit);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Process order through workflow stages
     * @param orderId Order ID
     * @param newStatus New status
     * @param notes Processing notes
     * @return true if successful
     */
    public boolean processOrder(int orderId, OrderStatus newStatus, String notes) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                return false;
            }

            // Validate status transition
            if (!isValidStatusTransition(order.getOrderStatus(), newStatus)) {
                return false;
            }

            // Handle special processing for certain status changes
            if (newStatus == OrderStatus.CANCELLED) {
                return cancelOrderWithInventoryRestore(orderId);
            } else if (newStatus == OrderStatus.DELIVERED) {
                return completeOrder(orderId);
            }

            return orderDAO.updateOrderStatus(orderId, newStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancel order and restore inventory
     * @param orderId Order ID
     * @return true if successful
     */
    private boolean cancelOrderWithInventoryRestore(int orderId) {
        try {
            // Get order items to restore inventory
            List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);

            // Restore inventory for each item
            for (OrderItem item : items) {
                Product product = productDAO.getProductById(item.getProductId());
                if (product != null) {
                    int restoredStock = product.getStockQuantity() + item.getQuantity();
                    product.setStockQuantity(restoredStock);
                    productDAO.updateProduct(product);
                }
            }

            // Update order status
            return orderDAO.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Complete order processing
     * @param orderId Order ID
     * @return true if successful
     */
    private boolean completeOrder(int orderId) {
        try {
            // Mark order as delivered
            return orderDAO.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate status transition
     * @param currentStatus Current order status
     * @param newStatus New order status
     * @return true if transition is valid
     */
    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
            case SHIPPED:
                return newStatus == OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELLED:
                return false; // Final states
            default:
                return false;
        }
    }

    /**
     * Get orders requiring attention (pending, low stock items)
     * @return List of orders needing attention
     */
    public List<Order> getOrdersRequiringAttention() {
        try {
            List<Order> allOrders = orderDAO.getAllOrders();
            List<Order> attentionOrders = new ArrayList<>();

            for (Order order : allOrders) {
                // Add pending orders older than 24 hours
                if (order.getOrderStatus() == OrderStatus.PENDING) {
                    long hoursSinceCreated = (System.currentTimeMillis() - order.getCreatedAt().getTime()) / (1000 * 60 * 60);
                    if (hoursSinceCreated > 24) {
                        attentionOrders.add(order);
                    }
                }

                // Add processing orders older than 48 hours
                if (order.getOrderStatus() == OrderStatus.PROCESSING) {
                    long hoursSinceCreated = (System.currentTimeMillis() - order.getCreatedAt().getTime()) / (1000 * 60 * 60);
                    if (hoursSinceCreated > 48) {
                        attentionOrders.add(order);
                    }
                }
            }

            return attentionOrders;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Get revenue by date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue
     */
    public BigDecimal getRevenueByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            BigDecimal totalRevenue = BigDecimal.ZERO;

            for (Order order : orders) {
                if (order.getOrderStatus() != OrderStatus.CANCELLED &&
                    order.getCreatedAt() != null &&
                    !order.getCreatedAt().before(startDate) &&
                    !order.getCreatedAt().after(endDate)) {
                    totalRevenue = totalRevenue.add(order.getTotalAmount());
                }
            }

            return totalRevenue;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
