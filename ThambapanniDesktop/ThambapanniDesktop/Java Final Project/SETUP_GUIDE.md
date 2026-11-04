# Thambapanni Sobha - Setup Guide

## 🗄️ Database Setup

### Step 1: Install MySQL
1. Download and install MySQL Server from https://dev.mysql.com/downloads/mysql/
2. During installation, set root password (or leave blank for no password)
3. Make sure MySQL service is running on port 3306

### Step 2: Create Database
Run one of these SQL files in MySQL:

**Option A: Quick Test Setup**
```sql
-- Run this file: test_database.sql
-- Creates minimal database with test users only
```

**Option B: Full Setup with Sample Data**
```sql
-- Run this file: database_setup.sql
-- Creates complete database with products, categories, orders, etc.
```

### Step 3: Run SQL File
```bash
# Method 1: MySQL Command Line
mysql -u root -p < test_database.sql

# Method 2: MySQL Workbench
# Open MySQL Workbench -> File -> Run SQL Script -> Select test_database.sql

# Method 3: Command Line (if no password)
mysql -u root < test_database.sql
```

## 🔑 Login Credentials

### Admin Account
- **Username:** admin
- **Password:** admin123

### Customer Account  
- **Username:** customer1
- **Password:** password123

## 🚀 Running the Application

### Method 1: Using Batch Files
```bash
# Test database connection first
run_test.bat

# Run the main application
run_app.bat
```

### Method 2: Manual Commands
```bash
# Test database connection
java -cp "../mysql-connector-j-9.3.0.jar;build/classes" test.DatabaseTest

# Run application
java -cp "../mysql-connector-j-9.3.0.jar;build/classes" thambapanni.main.JavaFinalProject
```

## 🔧 Troubleshooting

### Issue: "admin123 incorrect password"

**Possible Causes:**
1. Database not set up correctly
2. MySQL server not running
3. Connection parameters incorrect

**Solutions:**
1. **Run the database test first:**
   ```bash
   run_test.bat
   ```
   This will show you exactly what's happening with the database connection and authentication.

2. **Check MySQL connection parameters in DatabaseConnection.java:**
   - URL: `jdbc:mysql://localhost:3306/testsl`
   - Username: `root`
   - Password: `` (empty by default)

3. **Verify database setup:**
   ```sql
   USE testsl;
   SELECT * FROM users WHERE username = 'admin';
   ```

4. **Check MySQL service:**
   - Windows: Services -> MySQL -> Start
   - Or restart MySQL service

### Issue: Database Connection Failed

**Solutions:**
1. **Start MySQL service**
2. **Check port 3306 is available**
3. **Verify MySQL credentials in DatabaseConnection.java**
4. **Run test_database.sql to create fresh database**

### Issue: Application runs but no GUI appears

**Solutions:**
1. **Check console output for errors**
2. **Ensure Java Swing is supported**
3. **Try running with different Java version**

## 📁 File Structure
```
Java Final Project/
├── src/
│   ├── thambapanni/main/     # Main application
│   ├── model/                # Data models
│   ├── view/                 # GUI components
│   ├── controller/           # Business logic
│   ├── dao/                  # Database access
│   ├── database/             # Database utilities
│   └── test/                 # Test utilities
├── build/classes/            # Compiled classes
├── test_database.sql         # Quick database setup
├── database_setup.sql        # Full database with sample data
├── run_test.bat             # Test database connection
├── run_app.bat              # Run application
└── mysql-connector-j-9.3.0.jar  # MySQL driver (in parent folder)
```

## 🎯 Next Steps

1. **Run `run_test.bat` first** to verify database setup
2. **If test passes, run `run_app.bat`** to start the application
3. **Login with admin/admin123** to access admin features
4. **Login with customer1/password123** to test customer features

## 📞 Support

If you encounter issues:
1. Check the console output for detailed error messages
2. Run the database test to isolate connection issues
3. Verify MySQL is running and accessible
4. Ensure all SQL scripts have been executed successfully
