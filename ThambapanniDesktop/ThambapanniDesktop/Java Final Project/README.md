# Thambapanni Sobha - Sri Lankan Heritage E-commerce Desktop Application

## Overview
Thambapanni Sobha is a comprehensive e-commerce desktop application specifically designed for marketing and selling Sri Lankan heritage products. The application follows the Model-View-Controller (MVC) architectural pattern and provides both admin and customer interfaces.

## Features

### Phase 1 Implementation
- **User Authentication System**
  - Login and registration functionality
  - Role-based access control (Admin/Customer)
  - Secure password handling

- **Admin Dashboard**
  - Product management interface
  - Category management interface
  - User management interface
  - Order management interface
  - Reports and analytics (placeholder)

- **Customer Dashboard**
  - Product browsing interface
  - Shopping cart (placeholder)
  - Order history (placeholder)
  - Profile management (placeholder)

- **Database Integration**
  - MySQL database connectivity
  - Complete CRUD operations for all entities
  - Proper database schema with relationships

## Technical Architecture

### MVC Structure
```
src/
├── thambapanni/main/     # Main application entry point
├── model/                # Data models (User, Product, Category, Order, OrderItem)
├── view/                 # GUI components (Login, Registration, Dashboards)
├── controller/           # Business logic controllers
├── dao/                  # Data Access Objects for database operations
└── database/             # Database connection and initialization
```

### Database Schema
- **users**: User accounts with role-based access
- **categories**: Product categories (Spices, Tea, Ayurvedic Products, etc.)
- **products**: Sri Lankan heritage products with details
- **orders**: Customer orders with status tracking
- **order_items**: Individual items within orders

## Prerequisites
- Java 8 or higher
- MySQL Server running on localhost:3306
- Database named "test" (will be created automatically)
- MySQL Connector/J (included: mysql-connector-j-9.3.0.jar)

## Setup Instructions

1. **Database Setup**
   - Ensure MySQL server is running on localhost:3306
   - Create a database named "test" (or the application will create tables automatically)
   - Default credentials: username="root", password="" (can be modified in DatabaseConnection.java)

2. **Compilation**
   ```bash
   # Compile all source files
   javac -cp "../mysql-connector-j-9.3.0.jar" -d build/classes src/model/*.java
   javac -cp "../mysql-connector-j-9.3.0.jar;build/classes" -d build/classes src/database/*.java
   javac -cp "../mysql-connector-j-9.3.0.jar;build/classes" -d build/classes src/dao/*.java
   javac -cp "../mysql-connector-j-9.3.0.jar;build/classes" -d build/classes src/controller/*.java
   javac -cp "../mysql-connector-j-9.3.0.jar;build/classes" -d build/classes src/view/*.java
   javac -cp "../mysql-connector-j-9.3.0.jar;build/classes" -d build/classes src/thambapanni/main/*.java
   ```

3. **Running the Application**
   ```bash
   java -cp "../mysql-connector-j-9.3.0.jar;build/classes" thambapanni.main.JavaFinalProject
   ```

## Default Login Credentials
- **Admin Account**
  - Username: admin
  - Password: admin123

## Color Scheme
The application uses a Sri Lankan heritage-inspired color palette:
- Primary Color: Saddle Brown (#8B4513)
- Secondary Color: Peru (#CD853F)
- Accent Color: Gold (#FFD700)
- Background Color: Beige (#F5F5DC)
- Text Color: Dark Brown (#654321)

## Future Enhancements (Phase 2)
- Complete CRUD operations for all management interfaces
- Shopping cart functionality
- Order processing and payment integration
- Advanced reporting and analytics
- Product image management
- Email notifications
- Advanced search and filtering
- Inventory management
- Customer reviews and ratings

## Project Structure
This project demonstrates:
- Clean MVC architecture
- Database design and integration
- GUI development with Swing
- Object-oriented programming principles
- Error handling and validation
- Role-based access control

## Contributing
This is an educational project showcasing Sri Lankan heritage e-commerce platform development using Java desktop technologies.

## License
Educational use only - Promoting Sri Lankan Heritage Worldwide
