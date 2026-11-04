@echo off
echo Running Database Test...
echo.
java -cp "../mysql-connector-j-9.3.0.jar;build/classes" test.DatabaseTest
echo.
pause
