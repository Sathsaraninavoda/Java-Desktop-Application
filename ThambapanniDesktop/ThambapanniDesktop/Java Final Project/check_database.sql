-- Check Database Setup for Thambapanni Sobha
-- Run this to verify your database is set up correctly before adding orders

USE testsl;

-- Check if all tables exist
SELECT 'Checking database tables...' as Status;

SHOW TABLES;

-- Check users table structure and data
SELECT 'Users table structure:' as Info;
DESCRIBE users;

SELECT 'Users data:' as Info;
SELECT user_id, username, full_name, user_type FROM users;

-- Check products table structure and data
SELECT 'Products table structure:' as Info;
DESCRIBE products;

SELECT 'Products data (first 5):' as Info;
SELECT product_id, product_name, price, stock_quantity FROM products LIMIT 5;

-- Check orders table structure
SELECT 'Orders table structure:' as Info;
DESCRIBE orders;

-- Check order_items table structure
SELECT 'Order_items table structure:' as Info;
DESCRIBE order_items;

-- Check if there are any existing orders
SELECT 'Existing orders count:' as Info;
SELECT COUNT(*) as existing_orders FROM orders;

-- Check foreign key constraints
SELECT 'Foreign key constraints:' as Info;
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'testsl' 
AND REFERENCED_TABLE_NAME IS NOT NULL;
