package controller;

import dao.UserDAO;
import model.User;
import model.User.UserType;

/**
 * Controller for authentication and user management operations
 */
public class AuthController {
    
    private UserDAO userDAO;
    private static User currentUser = null;
    
    public AuthController() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Authenticate user login
     * @param username Username
     * @param password Password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        try {
            System.out.println("AuthController: Attempting to authenticate user: " + username);
            System.out.println("AuthController: Password provided: " + password);

            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                System.out.println("AuthController: Authentication successful for user: " + user.getUsername());
                System.out.println("AuthController: User type: " + user.getUserType());
                currentUser = user;
                return true;
            } else {
                System.out.println("AuthController: Authentication failed - user not found or invalid credentials");
            }
        } catch (Exception e) {
            System.out.println("AuthController: Exception during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Register a new user
     * @param username Username
     * @param password Password
     * @param email Email
     * @param fullName Full name
     * @param phone Phone number (optional)
     * @param address Address (optional)
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String username, String password, String email, 
                               String fullName, String phone, String address) {
        try {
            // Check if username or email already exists
            if (userDAO.usernameExists(username)) {
                return false;
            }
            
            if (userDAO.emailExists(email)) {
                return false;
            }
            
            // Create new user
            User newUser = new User(username, password, email, fullName);
            newUser.setPhone(phone);
            newUser.setAddress(address);
            newUser.setUserType(UserType.CUSTOMER); // Default to customer
            
            return userDAO.createUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get current logged-in user
     * @return Current user or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Check if current user is admin
     * @return true if current user is admin, false otherwise
     */
    public boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Check if current user is customer
     * @return true if current user is customer, false otherwise
     */
    public boolean isCurrentUserCustomer() {
        return currentUser != null && currentUser.isCustomer();
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        currentUser = null;
    }
    
    /**
     * Update current user's profile
     * @param email New email
     * @param fullName New full name
     * @param phone New phone
     * @param address New address
     * @return true if update successful, false otherwise
     */
    public boolean updateCurrentUserProfile(String email, String fullName, String phone, String address) {
        if (currentUser == null) {
            return false;
        }
        
        try {
            // Check if email is already used by another user
            User existingUser = userDAO.getUserByUsername(currentUser.getUsername());
            if (existingUser != null && !existingUser.getEmail().equals(email) && userDAO.emailExists(email)) {
                return false;
            }
            
            // Update user information
            currentUser.setEmail(email);
            currentUser.setFullName(fullName);
            currentUser.setPhone(phone);
            currentUser.setAddress(address);
            
            boolean success = userDAO.updateUser(currentUser);
            if (success) {
                // Refresh current user data from database
                currentUser = userDAO.getUserById(currentUser.getUserId());
            }
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Change current user's password
     * @param currentPassword Current password
     * @param newPassword New password
     * @return true if password change successful, false otherwise
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        try {
            // Verify current password
            if (!currentUser.getPassword().equals(currentPassword)) {
                return false;
            }
            
            // Update password
            currentUser.setPassword(newPassword);
            return userDAO.updateUser(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get user by ID (admin only)
     * @param userId User ID
     * @return User object or null if not found or not authorized
     */
    public User getUserById(int userId) {
        if (currentUser == null || !currentUser.isAdmin()) {
            return null;
        }
        
        try {
            return userDAO.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public boolean usernameExists(String username) {
        try {
            return userDAO.usernameExists(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        try {
            return userDAO.emailExists(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Validate user input
     * @param username Username
     * @param password Password
     * @param email Email
     * @param fullName Full name
     * @return Error message if validation fails, null if valid
     */
    public String validateUserInput(String username, String password, String email, String fullName) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required.";
        }
        
        if (username.length() < 3) {
            return "Username must be at least 3 characters long.";
        }
        
        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            return "Please enter a valid email address.";
        }
        
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Full name is required.";
        }
        
        return null; // Valid input
    }
}
