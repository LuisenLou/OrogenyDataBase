# Orogeny - Mountain Management Program

This is a class exercise that consists of developing a Java program to manage a mountain database. The program uses **XAMPP** as a local server, along with **phpMyAdmin** and **MySQL** to manage the database.

## Program Features

The program allows the following operations on a table called **Mountains**:

1. **Create a 'Mountains' table**: Creates a table in the database if it doesn't already exist.
2. **Add a new mountain**: Inserts mountain data into the table.
3. **View all mountains**: Displays all mountains stored in the table.
4. **View details of a specific mountain by ID**: Displays the details of a mountain based on its ID.
5. **Update mountain data**: Allows updating the details of an existing mountain.
6. **Delete a mountain**: Deletes a row (mountain) from the table based on its ID.
7. **Delete the 'Mountains' table**: Drops the table from the database entirely.

## System Requirements

- **Java 8** or later.
- **XAMPP**: Used to set up a local server with MySQL and phpMyAdmin.
- **JDBC Connection**: The MySQL JDBC connector is used to establish the connection between the Java program and the database.

## Initial Setup

1. **Install XAMPP**: XAMPP is a package that includes Apache, MySQL, and phpMyAdmin to create a local server. You can download and install it from [https://www.apachefriends.org/index.html](https://www.apachefriends.org/index.html).

2. **Run XAMPP**: Open the XAMPP control panel and make sure that the **Apache** and **MySQL** services are running. This will enable access to the local MySQL server and phpMyAdmin.

3. **Create the Database**:
   - Open **phpMyAdmin** by navigating to `http://localhost/phpmyadmin` in your browser.
   - Create a database named **orogeny**.

4. **Modify Database Credentials**:
   In the code, ensure that the database credentials match your local server setup:

   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/orogeny"; // Database URL
   private static final String USER = "root"; // Default MySQL user
   private static final String PASSWORD = ""; // Leave empty if no password is set

4. **Add MySQL Connector**:
   -  Download the mysql-connector-java.jar from the official MySQL site or from a repository like Maven.
   -  Include the JAR file in your CLASSPATH to allow the program to connect to the database.

## How to Run the Program

1. Ensure that XAMPP is running and both Apache and MySQL services are active.
2. Open the Java project in your preferred development environment (Eclipse, IntelliJ, etc.).
3. Compile and run the program.
4. Use the interactive menu to perform various operations on the database.

## Menu Structure

The program presents an interactive menu in the console with the following options:

1. Create a new 'Mountains' table (create table)
2. Add a Mountain to the Mountains table (add a row)
3. View all Mountains data (read table)
4. View details of a specific Mountain (read row)
5. Edit a Mountain's data (update row)
6. Delete a Mountain (delete row)
7. Drop the 'Mountains' table (delete table)
8. Exit



## Final Considerations

This program is a basic demonstration of using JDBC to interact with a MySQL database. XAMPP is recommended for local development, but this program can be adapted for any MySQL server by changing the database credentials in the code.

**Note:** This exercise is primarily educational and is not optimized for production use. For professional implementation, it's recommended to use advanced techniques for connection management, transaction control, and security (e.g., using strong passwords and encryption).



