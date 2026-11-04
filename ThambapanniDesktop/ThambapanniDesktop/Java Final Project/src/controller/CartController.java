package controller;

import model.CartItem;
import model.Product;
import controller.ProductController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for shopping cart operations
 */
public class CartController {
    
    private Map<Integer, CartItem> cartItems;
    private ProductController productController;
    
    public CartController() {
        this.cartItems = new HashMap<>();
        this.productController = new ProductController();
    }
    
    /**
     * Add product to cart
     * @param productId Product ID
     * @param quantity Quantity to add
     * @return true if successful, false otherwise
     */
    public boolean addToCart(int productId, int quantity) {
        try {
            Product product = productController.getProductById(productId);
            if (product == null) {
                return false;
            }
            
            // Check if product is in stock
            if (product.getStockQuantity() < quantity) {
                return false;
            }
            
            CartItem existingItem = cartItems.get(productId);
            if (existingItem != null) {
                // Update quantity if item already exists
                int newQuantity = existingItem.getQuantity() + quantity;
                if (newQuantity <= product.getStockQuantity()) {
                    existingItem.setQuantity(newQuantity);
                    return true;
                } else {
                    return false; // Not enough stock
                }
            } else {
                // Add new item to cart
                CartItem cartItem = new CartItem(product, quantity);
                cartItems.put(productId, cartItem);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Remove product from cart
     * @param productId Product ID
     * @return true if successful, false otherwise
     */
    public boolean removeFromCart(int productId) {
        try {
            return cartItems.remove(productId) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update quantity of item in cart
     * @param productId Product ID
     * @param quantity New quantity
     * @return true if successful, false otherwise
     */
    public boolean updateQuantity(int productId, int quantity) {
        try {
            if (quantity <= 0) {
                return removeFromCart(productId);
            }
            
            CartItem cartItem = cartItems.get(productId);
            if (cartItem == null) {
                return false;
            }
            
            // Check stock availability
            Product product = productController.getProductById(productId);
            if (product == null || product.getStockQuantity() < quantity) {
                return false;
            }
            
            cartItem.setQuantity(quantity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all items in cart
     * @return List of cart items
     */
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }
    
    /**
     * Get cart item by product ID
     * @param productId Product ID
     * @return CartItem or null if not found
     */
    public CartItem getCartItem(int productId) {
        return cartItems.get(productId);
    }
    
    /**
     * Get total number of items in cart
     * @return Total item count
     */
    public int getTotalItemCount() {
        return cartItems.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    /**
     * Get total price of all items in cart
     * @return Total price
     */
    public BigDecimal getTotalPrice() {
        return cartItems.values().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Check if cart is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * Clear all items from cart
     */
    public void clearCart() {
        cartItems.clear();
    }
    
    /**
     * Check if all items in cart are in stock
     * @return true if all items are available, false otherwise
     */
    public boolean validateStock() {
        for (CartItem item : cartItems.values()) {
            Product product = productController.getProductById(item.getProductId());
            if (product == null || product.getStockQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get items that are out of stock
     * @return List of out-of-stock items
     */
    public List<CartItem> getOutOfStockItems() {
        List<CartItem> outOfStock = new ArrayList<>();
        for (CartItem item : cartItems.values()) {
            Product product = productController.getProductById(item.getProductId());
            if (product == null || product.getStockQuantity() < item.getQuantity()) {
                outOfStock.add(item);
            }
        }
        return outOfStock;
    }
    
    /**
     * Get formatted total price
     * @return Formatted total price string
     */
    public String getFormattedTotalPrice() {
        return "LKR " + String.format("%.2f", getTotalPrice());
    }
    
    /**
     * Check if product is in cart
     * @param productId Product ID
     * @return true if product is in cart, false otherwise
     */
    public boolean isInCart(int productId) {
        return cartItems.containsKey(productId);
    }
    
    /**
     * Get quantity of specific product in cart
     * @param productId Product ID
     * @return Quantity in cart, 0 if not in cart
     */
    public int getQuantityInCart(int productId) {
        CartItem item = cartItems.get(productId);
        return item != null ? item.getQuantity() : 0;
    }
}
