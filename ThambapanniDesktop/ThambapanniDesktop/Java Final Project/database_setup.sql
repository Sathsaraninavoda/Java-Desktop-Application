-- Thambapanni Sobha Database Setup Script
-- Sri Lankan Heritage E-commerce Platform
-- Run this script in MySQL to set up the database

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS testsl;
USE testsl;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    user_type ENUM('ADMIN', 'CUSTOMER') DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    category_id INT,
    image_url VARCHAR(500),
    origin VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- Create orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status ENUM('PENDING', 'PROCESSING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Create order_items table
CREATE TABLE order_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Insert default admin user (using INSERT IGNORE to avoid duplicates)
INSERT IGNORE INTO users (username, password, email, full_name, user_type) VALUES
('admin', 'admin123', 'admin@thambapanni.lk', 'System Administrator', 'ADMIN');

-- Insert sample customer users (using INSERT IGNORE to avoid duplicates)
INSERT IGNORE INTO users (username, password, email, full_name, phone, address, user_type) VALUES
('customer1', 'password123', 'customer1@email.com', 'Saman Perera', '+94771234567', '123 Galle Road, Colombo 03', 'CUSTOMER'),
('customer2', 'password123', 'customer2@email.com', 'Nimal Silva', '+94712345678', '456 Kandy Road, Kandy', 'CUSTOMER'),
('customer3', 'password123', 'customer3@email.com', 'Kamala Fernando', '+94723456789', '789 Negombo Road, Negombo', 'CUSTOMER');

-- Insert product categories (using INSERT IGNORE to avoid duplicates)
INSERT IGNORE INTO categories (category_name, description) VALUES
('Spices', 'Traditional Sri Lankan spices and seasonings including cinnamon, cardamom, cloves, and pepper'),
('Tea', 'Premium Ceylon tea varieties from different regions of Sri Lanka'),
('Ayurvedic Products', 'Traditional herbal medicines and wellness products based on ancient Ayurvedic practices'),
('Handicrafts', 'Traditional Sri Lankan handicrafts including wood carvings, masks, and pottery'),
('Textiles', 'Traditional fabrics, batik, and clothing items showcasing Sri Lankan craftsmanship'),
('Gems & Jewelry', 'Precious and semi-precious gemstones and traditional jewelry from Sri Lanka'),
('Food Products', 'Traditional Sri Lankan food items, snacks, and preserved foods');

-- Insert sample products
INSERT INTO products (product_name, description, price, stock_quantity, category_id, origin) VALUES 
-- Spices
('Ceylon Cinnamon Sticks', 'Authentic Ceylon cinnamon sticks from the hills of Sri Lanka. Known for its sweet and delicate flavor.', 850.00, 50, 1, 'Matale District'),
('Organic Black Pepper', 'Premium quality black pepper grown in organic farms. Perfect for seasoning and cooking.', 1200.00, 30, 1, 'Matale District'),
('Cardamom Pods', 'Fresh green cardamom pods with intense aroma. Essential for Sri Lankan cuisine and tea.', 2500.00, 25, 1, 'Kandy District'),
('Cloves', 'High-quality whole cloves with strong aromatic properties. Used in cooking and traditional medicine.', 1800.00, 40, 1, 'Southern Province'),

-- Tea
('Ceylon Gold Tea', 'Premium black tea from high-grown estates. Rich flavor with golden color.', 1500.00, 100, 2, 'Nuwara Eliya'),
('Earl Grey Ceylon', 'Classic Earl Grey blend with Ceylon tea base and bergamot oil.', 1800.00, 75, 2, 'Dimbula Valley'),
('Green Tea', 'Organic Ceylon green tea with antioxidant properties and fresh taste.', 2200.00, 60, 2, 'Uva Province'),
('White Tea', 'Rare Ceylon white tea with delicate flavor and health benefits.', 4500.00, 20, 2, 'Nuwara Eliya'),

-- Ayurvedic Products
('Neem Oil', 'Pure neem oil for skin care and medicinal purposes. 100% natural and organic.', 950.00, 35, 3, 'Western Province'),
('Turmeric Powder', 'Organic turmeric powder with high curcumin content. Anti-inflammatory properties.', 650.00, 80, 3, 'North Central Province'),
('Gotu Kola Capsules', 'Traditional brain tonic and memory enhancer in convenient capsule form.', 1200.00, 45, 3, 'Central Province'),

-- Handicrafts
('Wooden Elephant Carving', 'Hand-carved wooden elephant sculpture. Traditional Sri Lankan craftsmanship.', 3500.00, 15, 4, 'Ambalangoda'),
('Traditional Mask', 'Authentic Sri Lankan devil mask used in traditional dance performances.', 2800.00, 20, 4, 'Ambalangoda'),
('Clay Pottery Set', 'Traditional clay pottery set including bowls and water jug.', 1800.00, 25, 4, 'Kelaniya'),

-- Textiles
('Batik Sarong', 'Traditional batik sarong with intricate patterns and vibrant colors.', 2200.00, 30, 5, 'Kandy'),
('Handloom Cotton Shirt', 'Comfortable cotton shirt made from handloom fabric.', 3200.00, 40, 5, 'Kurunegala'),
('Silk Saree', 'Elegant silk saree with traditional Sri Lankan designs and gold thread work.', 15000.00, 10, 5, 'Colombo');

-- Insert sample orders
INSERT INTO orders (user_id, total_amount, order_status, shipping_address) VALUES 
(2, 3350.00, 'DELIVERED', '123 Galle Road, Colombo 03'),
(3, 5700.00, 'SHIPPED', '456 Kandy Road, Kandy'),
(4, 2450.00, 'PENDING', '789 Negombo Road, Negombo'),
(2, 1800.00, 'CONFIRMED', '123 Galle Road, Colombo 03');

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES 
-- Order 1 items
(1, 1, 2, 850.00),  -- 2x Ceylon Cinnamon
(1, 5, 1, 1500.00), -- 1x Ceylon Gold Tea
(1, 9, 1, 950.00),  -- 1x Neem Oil

-- Order 2 items
(2, 8, 1, 4500.00), -- 1x White Tea
(2, 15, 1, 1200.00), -- 1x Handloom Cotton Shirt

-- Order 3 items
(3, 2, 1, 1200.00), -- 1x Black Pepper
(3, 4, 1, 1800.00), -- 1x Cloves

-- Order 4 items
(4, 13, 1, 1800.00); -- 1x Clay Pottery Set

-- Display setup completion message
SELECT 'Database setup completed successfully!' as Status;
SELECT 'Default Admin Credentials: username = admin, password = admin123' as Admin_Login;
SELECT 'Sample Customer Credentials: username = customer1, password = password123' as Customer_Login;

-- Display table counts
SELECT 
    (SELECT COUNT(*) FROM users) as Total_Users,
    (SELECT COUNT(*) FROM categories) as Total_Categories,
    (SELECT COUNT(*) FROM products) as Total_Products,
    (SELECT COUNT(*) FROM orders) as Total_Orders,
    (SELECT COUNT(*) FROM order_items) as Total_Order_Items;
