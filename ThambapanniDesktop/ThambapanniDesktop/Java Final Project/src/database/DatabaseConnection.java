package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection utility class for Thambapanni Sobha application
 * Manages MySQL database connections and initialization
 */
public class DatabaseConnection {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testsl";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;
    
    /**
     * Get database connection instance (Singleton pattern)
     * @return Connection object
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER_CLASS);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize database tables if they don't exist
     */
    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            if (conn == null) {
                System.out.println("Database connection not available. Skipping initialization.");
                return;
            }
            Statement stmt = conn.createStatement();
            
            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
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
                )
            """;
            
            // Create categories table
            String createCategoriesTable = """
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INT AUTO_INCREMENT PRIMARY KEY,
                    category_name VARCHAR(100) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Create products table
            String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
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
                    FOREIGN KEY (category_id) REFERENCES categories(category_id)
                )
            """;
            
            // Create orders table
            String createOrdersTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    order_status ENUM('PENDING', 'PROCESSING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
                    shipping_address TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                )
            """;
            
            // Create order_items table
            String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    item_id INT AUTO_INCREMENT PRIMARY KEY,
                    order_id INT NOT NULL,
                    product_id INT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id),
                    FOREIGN KEY (product_id) REFERENCES products(product_id)
                )
            """;
            
            // Execute table creation statements
            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createOrdersTable);
            stmt.execute(createOrderItemsTable);
            
            // Insert default admin user if not exists
            String insertAdmin = """
                INSERT IGNORE INTO users (username, password, email, full_name, user_type) 
                VALUES ('admin', 'admin123', 'admin@thambapanni.lk', 'System Administrator', 'ADMIN')
            """;
            stmt.execute(insertAdmin);
            
            // Insert sample categories
            String insertCategories = """
                INSERT IGNORE INTO categories (category_name, description) VALUES
                ('Spices', 'Traditional Sri Lankan spices and seasonings'),
                ('Tea', 'Ceylon tea varieties from different regions'),
                ('Ayurvedic Products', 'Traditional herbal medicines and wellness products'),
                ('Handicrafts', 'Traditional Sri Lankan handicrafts and artwork'),
                ('Textiles', 'Traditional fabrics and clothing'),
                ('Coconut Products', 'Pure coconut oil, desiccated coconut, and coconut-based products'),
                ('Gemstones', 'Authentic Sri Lankan precious and semi-precious stones'),
                ('Essential Oils', 'Natural essential oils extracted from Sri Lankan plants')
            """;
            stmt.execute(insertCategories);

            // Insert sample products
            String insertProducts = """
                INSERT IGNORE INTO products (product_name, description, price, stock_quantity, category_id, image_url, origin) VALUES
                ('Ceylon Cinnamon Sticks', 'Premium quality true cinnamon from Sri Lanka, sweet and delicate flavor', 15.99, 100, 1, 'images/cinnamon.jpg', 'Kandy'),
                ('Black Pepper Whole', 'Organic black peppercorns with intense flavor and aroma', 12.50, 80, 1, 'images/pepper.jpg', 'Matale'),
                ('Cardamom Pods', 'Green cardamom pods, perfect for tea and desserts', 25.00, 60, 1, 'images/cardamom.jpg', 'Kandy'),
                ('Turmeric Powder', 'Pure turmeric powder with high curcumin content', 8.99, 120, 1, 'images/turmeric.jpg', 'Galle'),
                ('Ceylon Black Tea PEKOE', 'High-grown Ceylon black tea with rich flavor', 18.99, 150, 2, 'images/black_tea.jpg', 'Nuwara Eliya'),
                ('Earl Grey Ceylon Tea', 'Ceylon black tea blended with bergamot oil', 22.50, 90, 2, 'images/earl_grey.jpg', 'Dimbula'),
                ('Green Tea Sencha Style', 'Ceylon green tea processed in Japanese style', 20.00, 75, 2, 'images/green_tea.jpg', 'Uva'),
                ('White Tea Silver Tips', 'Premium white tea with delicate flavor', 45.00, 40, 2, 'images/white_tea.jpg', 'Nuwara Eliya'),
                ('Ashwagandha Capsules', 'Natural stress relief and energy booster', 35.99, 50, 3, 'images/ashwagandha.jpg', 'Colombo'),
                ('Triphala Powder', 'Traditional Ayurvedic digestive support', 28.50, 65, 3, 'images/triphala.jpg', 'Kandy'),
                ('Neem Oil', 'Pure neem oil for skin and hair care', 16.99, 85, 3, 'images/neem_oil.jpg', 'Anuradhapura'),
                ('Wooden Elephant Carving', 'Hand-carved elephant from sustainable wood', 89.99, 25, 4, 'images/elephant.jpg', 'Kandy'),
                ('Batik Wall Hanging', 'Traditional batik art piece', 125.00, 15, 4, 'images/batik.jpg', 'Colombo'),
                ('Brass Buddha Statue', 'Handcrafted brass Buddha statue', 199.99, 10, 4, 'images/buddha.jpg', 'Kandy'),
                ('Handloom Cotton Saree', 'Traditional handwoven cotton saree', 150.00, 30, 5, 'images/saree.jpg', 'Kurunegala'),
                ('Batik Shirt', 'Comfortable cotton shirt with batik patterns', 45.00, 50, 5, 'images/batik_shirt.jpg', 'Colombo'),
                ('Virgin Coconut Oil', 'Cold-pressed virgin coconut oil', 24.99, 100, 6, 'images/coconut_oil.jpg', 'Galle'),
                ('Desiccated Coconut', 'Fine grade desiccated coconut', 12.99, 80, 6, 'images/desiccated_coconut.jpg', 'Galle'),
                ('Blue Sapphire', 'Natural Ceylon blue sapphire, 2 carats', 2500.00, 5, 7, 'images/sapphire.jpg', 'Ratnapura'),
                ('Star Ruby', 'Natural star ruby with asterism effect', 1800.00, 3, 7, 'images/ruby.jpg', 'Ratnapura'),
                ('Citronella Oil', 'Pure citronella essential oil', 18.99, 60, 8, 'images/citronella.jpg', 'Matale'),
                ('Lemongrass Oil', 'Organic lemongrass essential oil', 22.50, 45, 8, 'images/lemongrass.jpg', 'Kandy')
            """;
            stmt.execute(insertProducts);
            
            System.out.println("Database tables initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database tables!");
            e.printStackTrace();
        }
    }
}
