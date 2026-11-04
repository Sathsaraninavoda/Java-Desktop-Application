-- Simple Sample Orders Data for Thambapanni Sobha
-- This is a simplified version that's easier to debug
-- Run this after setting up the main database with database_setup.sql

USE testsl;

-- First, let's check if we have the required data
SELECT 'Checking existing data...' as Status;
SELECT COUNT(*) as user_count FROM users WHERE user_type = 'CUSTOMER';
SELECT COUNT(*) as product_count FROM products;

-- Insert a few simple orders one by one
-- Order 1: Customer 1 order
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES
(2, 2050.00, 'DELIVERED', '123 Galle Road, Colombo 03');

-- Get the order_id for the order we just inserted
SET @order1_id = LAST_INSERT_ID();

-- Insert order items for Order 1
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@order1_id, 1, 2, 850.00),
(@order1_id, 2, 1, 350.00);

-- Order 2: Customer 2 order
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES
(3, 1500.00, 'PROCESSING', '456 Kandy Road, Kandy');

SET @order2_id = LAST_INSERT_ID();

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@order2_id, 3, 1, 1500.00);

-- Order 3: Customer 3 order
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES
(4, 950.00, 'PENDING', '789 Negombo Road, Negombo');

SET @order3_id = LAST_INSERT_ID();

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@order3_id, 1, 1, 950.00);

-- Order 4: Another order for Customer 1
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES
(2, 3200.00, 'SHIPPED', '123 Galle Road, Colombo 03');

SET @order4_id = LAST_INSERT_ID();

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@order4_id, 4, 2, 1600.00);

-- Order 5: Another order for Customer 2
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES
(3, 2800.00, 'CONFIRMED', '456 Kandy Road, Kandy');

SET @order5_id = LAST_INSERT_ID();

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@order5_id, 5, 1, 2800.00);

-- Verification
SELECT 'Sample orders created successfully!' as Status;

-- Show all orders
SELECT 
    o.order_id,
    u.full_name as customer_name,
    o.total_amount,
    o.order_status,
    o.created_at
FROM orders o
JOIN users u ON o.user_id = u.user_id
ORDER BY o.order_id;

-- Show order items
SELECT 
    oi.order_id,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    (oi.quantity * oi.unit_price) as total_price
FROM order_items oi
JOIN products p ON oi.product_id = p.product_id
ORDER BY oi.order_id;

-- Show order summary by status
SELECT 
    order_status,
    COUNT(*) as order_count,
    SUM(total_amount) as total_revenue
FROM orders 
GROUP BY order_status;
