package test;

import database.DatabaseConnection;
import dao.UserDAO;
import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Simple database connection and authentication test
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        
        // Test 1: Database Connection
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful");
                
                // Test 2: Check if users table exists and has data
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (rs.next()) {
                    int userCount = rs.getInt("count");
                    System.out.println("✓ Users table exists with " + userCount + " users");
                }
                
                // Test 3: Check admin user specifically
                rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
                if (rs.next()) {
                    System.out.println("✓ Admin user found:");
                    System.out.println("  - Username: " + rs.getString("username"));
                    System.out.println("  - Password: " + rs.getString("password"));
                    System.out.println("  - Email: " + rs.getString("email"));
                    System.out.println("  - User Type: " + rs.getString("user_type"));
                } else {
                    System.out.println("✗ Admin user not found in database");
                }
                
            } else {
                System.out.println("✗ Database connection failed");
                return;
            }
        } catch (Exception e) {
            System.out.println("✗ Database connection error: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        System.out.println("\n=== Authentication Test ===");
        
        // Test 4: UserDAO Authentication
        UserDAO userDAO = new UserDAO();
        
        // Test admin login
        System.out.println("Testing admin login...");
        User adminUser = userDAO.authenticateUser("admin", "admin123");
        if (adminUser != null) {
            System.out.println("✓ Admin authentication successful");
            System.out.println("  - User ID: " + adminUser.getUserId());
            System.out.println("  - Full Name: " + adminUser.getFullName());
            System.out.println("  - User Type: " + adminUser.getUserType());
        } else {
            System.out.println("✗ Admin authentication failed");
        }
        
        // Test customer login
        System.out.println("\nTesting customer login...");
        User customerUser = userDAO.authenticateUser("customer1", "password123");
        if (customerUser != null) {
            System.out.println("✓ Customer authentication successful");
            System.out.println("  - User ID: " + customerUser.getUserId());
            System.out.println("  - Full Name: " + customerUser.getFullName());
            System.out.println("  - User Type: " + customerUser.getUserType());
        } else {
            System.out.println("✗ Customer authentication failed");
        }
        
        // Test invalid login
        System.out.println("\nTesting invalid login...");
        User invalidUser = userDAO.authenticateUser("invalid", "invalid");
        if (invalidUser == null) {
            System.out.println("✓ Invalid login correctly rejected");
        } else {
            System.out.println("✗ Invalid login incorrectly accepted");
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
