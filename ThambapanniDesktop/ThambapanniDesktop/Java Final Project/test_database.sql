-- Simple test database setup for Thambapanni Sobha
-- Run this first to test the login functionality

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

-- Insert test users with exact credentials
INSERT INTO users (username, password, email, full_name, user_type) VALUES 
('admin', 'admin123', 'admin@thambapanni.lk', 'System Administrator', 'ADMIN');

INSERT INTO users (username, password, email, full_name, phone, address, user_type) VALUES 
('customer1', 'password123', 'customer1@email.com', 'Saman Perera', '+94771234567', '123 Galle Road, Colombo 03', 'CUSTOMER');

-- Verify the data was inserted correctly
SELECT 'Users created successfully!' as Status;
SELECT user_id, username, password, email, full_name, user_type FROM users;

-- Test the exact query that the Java application will use
SELECT 'Testing admin login...' as Test;
SELECT * FROM users WHERE username = 'admin' AND password = 'admin123';

SELECT 'Testing customer login...' as Test;
SELECT * FROM users WHERE username = 'customer1' AND password = 'password123';

-- Show final status
SELECT 
    (SELECT COUNT(*) FROM users WHERE user_type = 'ADMIN') as Admin_Count,
    (SELECT COUNT(*) FROM users WHERE user_type = 'CUSTOMER') as Customer_Count,
    (SELECT COUNT(*) FROM users) as Total_Users;
