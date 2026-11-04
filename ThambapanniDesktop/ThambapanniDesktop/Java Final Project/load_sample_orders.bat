@echo off
echo Loading Sample Orders Data for Thambapanni Sobha...
echo.
echo This script will add sample order data to your database.
echo Make sure you have already run the main database setup first.
echo.
pause

echo Connecting to MySQL and loading sample orders...
mysql -u root -p testsl < sample_orders_data.sql

echo.
echo Sample orders data loaded successfully!
echo You can now test the order management and reports functionality.
echo.
pause
