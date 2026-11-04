-- Sample Orders Data for Thambapanni Sobha E-commerce Platform
-- This script adds sample order data for testing order management and reporting functionality
-- Run this after setting up the main database with database_setup.sql

USE testsl;

-- Insert sample orders with various statuses and dates
-- Note: Make sure users and products exist before running this script

-- Recent orders (last 7 days)
-- Customer 1 orders
INSERT INTO orders (user_id, total_amount, order_status, shipping_address, created_at, updated_at) VALUES
(2, 2050.00, 'DELIVERED', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 1850.00, 'SHIPPED', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 2 DAYS), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 3200.00, 'PROCESSING', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 3 DAYS), DATE_SUB(NOW(), INTERVAL 2 DAYS));

-- Customer 2 orders
INSERT INTO orders (user_id, total_amount, order_status, shipping_address, created_at, updated_at) VALUES
(3, 1500.00, 'CONFIRMED', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 2 DAYS), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 2800.00, 'DELIVERED', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 4 DAYS), DATE_SUB(NOW(), INTERVAL 3 DAYS)),
(3, 950.00, 'PENDING', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- Customer 3 orders
INSERT INTO orders (user_id, total_amount, order_status, shipping_address, created_at, updated_at) VALUES
(4, 4200.00, 'SHIPPED', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 3 DAYS), DATE_SUB(NOW(), INTERVAL 2 DAYS)),
(4, 1750.00, 'PROCESSING', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 5 DAYS), DATE_SUB(NOW(), INTERVAL 4 DAYS)),
(4, 890.00, 'CANCELLED', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 6 DAYS), DATE_SUB(NOW(), INTERVAL 5 DAYS));

-- Older orders (last 30 days)
INSERT INTO orders (user_id, total_amount, order_status, shipping_address, created_at, updated_at) VALUES
(2, 1200.00, 'DELIVERED', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 8 DAYS), DATE_SUB(NOW(), INTERVAL 7 DAYS)),
(3, 2100.00, 'DELIVERED', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 10 DAYS), DATE_SUB(NOW(), INTERVAL 9 DAYS)),
(4, 3500.00, 'DELIVERED', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 12 DAYS), DATE_SUB(NOW(), INTERVAL 11 DAYS)),
(2, 1800.00, 'DELIVERED', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 15 DAYS), DATE_SUB(NOW(), INTERVAL 14 DAYS)),
(3, 2600.00, 'DELIVERED', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 18 DAYS), DATE_SUB(NOW(), INTERVAL 17 DAYS)),
(4, 1400.00, 'CANCELLED', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 20 DAYS), DATE_SUB(NOW(), INTERVAL 19 DAYS)),
(2, 2900.00, 'DELIVERED', '123 Galle Road, Colombo 03', DATE_SUB(NOW(), INTERVAL 22 DAYS), DATE_SUB(NOW(), INTERVAL 21 DAYS)),
(3, 1650.00, 'DELIVERED', '456 Kandy Road, Kandy', DATE_SUB(NOW(), INTERVAL 25 DAYS), DATE_SUB(NOW(), INTERVAL 24 DAYS)),
(4, 3800.00, 'DELIVERED', '789 Negombo Road, Negombo', DATE_SUB(NOW(), INTERVAL 28 DAYS), DATE_SUB(NOW(), INTERVAL 27 DAYS));

-- Insert corresponding order items for each order
-- Using LAST_INSERT_ID() and variables to get the correct order_ids

-- Get the starting order_id after all orders are inserted
SET @start_order_id = (SELECT MAX(order_id) FROM orders) - 17;

-- Order items for Customer 1 orders (first 3 orders)
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id, 1, 2, 850.00),      -- Ceylon Cinnamon Sticks
(@start_order_id, 2, 1, 1200.00);     -- Organic Black Pepper

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 1, 3, 1, 1850.00); -- Ceylon Tea Collection

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 2, 4, 2, 1600.00); -- Ayurvedic Herbal Oil

-- Order items for Customer 2 orders (next 3 orders)
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 3, 5, 1, 1500.00); -- Traditional Wood Carving

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 4, 1, 1, 850.00),  -- Ceylon Cinnamon Sticks
(@start_order_id + 4, 6, 1, 1950.00); -- Batik Fabric

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 5, 2, 1, 950.00);  -- Organic Black Pepper

-- Order items for Customer 3 orders (next 3 orders)
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 6, 7, 1, 4200.00); -- Blue Sapphire Ring

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 7, 3, 1, 1750.00); -- Ceylon Tea Collection

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 8, 1, 1, 890.00);  -- Ceylon Cinnamon Sticks

-- Order items for older orders
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 9, 2, 1, 1200.00); -- Organic Black Pepper

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 10, 8, 1, 2100.00); -- Traditional Curry Powder Set

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 11, 4, 2, 1750.00); -- Ayurvedic Herbal Oil

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 12, 3, 1, 1800.00); -- Ceylon Tea Collection

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 13, 6, 1, 2600.00); -- Batik Fabric

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 14, 1, 1, 1400.00); -- Ceylon Cinnamon Sticks

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 15, 5, 1, 2900.00); -- Traditional Wood Carving

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 16, 2, 1, 1650.00); -- Organic Black Pepper

INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
(@start_order_id + 17, 7, 1, 3800.00); -- Blue Sapphire Ring

-- Verification queries
SELECT 'Sample orders created successfully!' as Status;

-- Show order summary
SELECT 
    order_status,
    COUNT(*) as order_count,
    SUM(total_amount) as total_revenue
FROM orders 
GROUP BY order_status
ORDER BY order_count DESC;

-- Show recent orders (last 7 days)
SELECT 
    o.order_id,
    u.full_name as customer_name,
    o.total_amount,
    o.order_status,
    o.created_at
FROM orders o
JOIN users u ON o.user_id = u.user_id
WHERE o.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
ORDER BY o.created_at DESC;

-- Show total sales by day (last 7 days)
SELECT 
    DATE(created_at) as order_date,
    COUNT(*) as orders_count,
    SUM(total_amount) as daily_sales
FROM orders 
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
GROUP BY DATE(created_at)
ORDER BY order_date DESC;

-- Show product sales summary
SELECT 
    p.product_name,
    SUM(oi.quantity) as total_quantity_sold,
    SUM(oi.quantity * oi.unit_price) as total_revenue
FROM order_items oi
JOIN products p ON oi.product_id = p.product_id
JOIN orders o ON oi.order_id = o.order_id
WHERE o.order_status != 'CANCELLED'
GROUP BY p.product_id, p.product_name
ORDER BY total_revenue DESC;
