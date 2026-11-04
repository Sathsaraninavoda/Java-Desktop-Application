package dao;

import database.DatabaseConnection;
import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for OrderItem operations
 */
public class OrderItemDAO {
    
    /**
     * Create a new order item
     * @param orderItem OrderItem object to create
     * @return true if successful, false otherwise
     */
    public boolean createOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getProductId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setBigDecimal(4, orderItem.getUnitPrice());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderItem.setItemId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get order items by order ID
     * @param orderId Order ID
     * @return List of order items for the order
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = """
            SELECT oi.*, p.product_name, p.description as product_description 
            FROM order_items oi 
            LEFT JOIN products p ON oi.product_id = p.product_id 
            WHERE oi.order_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(mapResultSetToOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }
    
    /**
     * Get order item by ID
     * @param itemId Item ID
     * @return OrderItem object if found, null otherwise
     */
    public OrderItem getOrderItemById(int itemId) {
        String sql = """
            SELECT oi.*, p.product_name, p.description as product_description 
            FROM order_items oi 
            LEFT JOIN products p ON oi.product_id = p.product_id 
            WHERE oi.item_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update order item
     * @param orderItem OrderItem object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET quantity = ?, unit_price = ? WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItem.getQuantity());
            pstmt.setBigDecimal(2, orderItem.getUnitPrice());
            pstmt.setInt(3, orderItem.getItemId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete order item by ID
     * @param itemId Item ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrderItem(int itemId) {
        String sql = "DELETE FROM order_items WHERE item_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete all order items for an order
     * @param orderId Order ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrderItemsByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() >= 0; // >= 0 because it's ok if no items exist
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get total quantity sold for a product
     * @param productId Product ID
     * @return Total quantity sold
     */
    public int getTotalQuantitySoldByProduct(int productId) {
        String sql = """
            SELECT SUM(oi.quantity) 
            FROM order_items oi 
            JOIN orders o ON oi.order_id = o.order_id 
            WHERE oi.product_id = ? AND o.order_status != 'CANCELLED'
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Get best selling products
     * @param limit Number of products to return
     * @return List of order items representing best selling products
     */
    public List<OrderItem> getBestSellingProducts(int limit) {
        List<OrderItem> bestSellers = new ArrayList<>();
        String sql = """
            SELECT oi.product_id, p.product_name, p.description as product_description, 
                   SUM(oi.quantity) as total_quantity, AVG(oi.unit_price) as avg_price
            FROM order_items oi 
            JOIN products p ON oi.product_id = p.product_id 
            JOIN orders o ON oi.order_id = o.order_id 
            WHERE o.order_status != 'CANCELLED'
            GROUP BY oi.product_id, p.product_name, p.description 
            ORDER BY total_quantity DESC 
            LIMIT ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductDescription(rs.getString("product_description"));
                    item.setQuantity(rs.getInt("total_quantity"));
                    item.setUnitPrice(rs.getBigDecimal("avg_price"));
                    bestSellers.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bestSellers;
    }
    
    /**
     * Map ResultSet to OrderItem object
     * @param rs ResultSet
     * @return OrderItem object
     * @throws SQLException
     */
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setProductId(rs.getInt("product_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setProductDescription(rs.getString("product_description"));
        return orderItem;
    }
}
