package controller;

import dao.UserDAO;
import model.User;
import model.User.UserType;

import java.util.List;

/**
 * Controller for user management operations (separate from authentication)
 */
public class UserController {
    
    private UserDAO userDAO;
    
    public UserController() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Create a new user (admin only)
     * @param user User to create
     * @return true if successful, false otherwise
     */
    public boolean createUser(User user) {
        try {
            // Validate username doesn't already exist
            if (userDAO.usernameExists(user.getUsername())) {
                return false;
            }
            
            // Validate email doesn't already exist
            if (userDAO.emailExists(user.getEmail())) {
                return false;
            }
            
            return userDAO.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User object or null if not found
     */
    public User getUserById(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get user by username
     * @param username Username
     * @return User object or null if not found
     */
    public User getUserByUsername(String username) {
        try {
            return userDAO.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list on error
        }
    }
    
    /**
     * Get users by type
     * @param userType User type (ADMIN or CUSTOMER)
     * @return List of users with the specified type
     */
    public List<User> getUsersByType(UserType userType) {
        try {
            List<User> allUsers = userDAO.getAllUsers();
            return allUsers.stream()
                    .filter(user -> user.getUserType() == userType)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Update user information
     * @param user User with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        try {
            // Check if user exists
            User existingUser = userDAO.getUserById(user.getUserId());
            if (existingUser == null) {
                return false;
            }
            
            // Check if new username conflicts with existing user (excluding current one)
            if (!existingUser.getUsername().equals(user.getUsername()) &&
                userDAO.usernameExists(user.getUsername())) {
                return false;
            }
            
            // Check if new email conflicts with existing user (excluding current one)
            if (!existingUser.getEmail().equals(user.getEmail()) &&
                userDAO.emailExists(user.getEmail())) {
                return false;
            }
            
            return userDAO.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete user by ID (admin only)
     * @param userId User ID
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        try {
            // Check if user exists
            User user = userDAO.getUserById(userId);
            if (user == null) {
                return false;
            }
            
            // Prevent deletion of admin users (optional business rule)
            if (user.getUserType() == UserType.ADMIN) {
                return false; // Cannot delete admin users
            }
            
            return userDAO.deleteUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
            return false;
        }
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
            return false;
        }
    }
    
    /**
     * Change user password (admin only)
     * @param userId User ID
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean changeUserPassword(int userId, String newPassword) {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                return false;
            }
            
            user.setPassword(newPassword);
            return userDAO.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Change user type (admin only)
     * @param userId User ID
     * @param newUserType New user type
     * @return true if successful, false otherwise
     */
    public boolean changeUserType(int userId, UserType newUserType) {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                return false;
            }
            
            user.setUserType(newUserType);
            return userDAO.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get customer count
     * @return Number of customer users
     */
    public int getCustomerCount() {
        try {
            return (int) userDAO.getAllUsers().stream()
                    .filter(user -> user.getUserType() == UserType.CUSTOMER)
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Get admin count
     * @return Number of admin users
     */
    public int getAdminCount() {
        try {
            return (int) userDAO.getAllUsers().stream()
                    .filter(user -> user.getUserType() == UserType.ADMIN)
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Check if user can be deleted (has no orders)
     * @param userId User ID
     * @return true if can be deleted, false otherwise
     */
    public boolean canDeleteUser(int userId) {
        try {
            // This would require checking if user has orders
            // For now, we'll allow deletion of non-admin users
            User user = userDAO.getUserById(userId);
            return user != null && user.getUserType() != UserType.ADMIN;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Validate user data
     * @param user User to validate
     * @return true if valid, false otherwise
     */
    public boolean validateUser(User user) {
        if (user == null) return false;
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) return false;
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) return false;
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) return false;
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) return false;
        if (user.getUserType() == null) return false;
        
        // Basic email validation
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) return false;
        
        return true;
    }
}
