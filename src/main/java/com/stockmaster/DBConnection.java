package com.stockmaster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
  protected static final String host = "localhost";
  protected static final int port = 3306;
  protected static final String user = "root";
  protected static final String password = "$Root55";
  protected static final String dbName = "stockmaster";
  protected static final String mysqlServerUrl = "jdbc:mysql://" + host + ":" + port;

  private Connection connection;

  protected Connection setupDatabaseConnection() {

    try {
      // Connect to MySQL server
      connection = DriverManager.getConnection(mysqlServerUrl, user, password);

      // Create the database if it doesn't exist
      String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + dbName;
      String useDatabaseSql = "USE " + dbName;
      Statement statement = connection.createStatement();
      statement.executeUpdate(createDatabaseSql);
      statement.executeUpdate(useDatabaseSql);

      // create database tables
      // createTables();

    } catch (SQLException e) {
      System.err.println("Error connecting to MySQL server: " + e.getMessage());
    }
    return connection;
  }

  private void createTables() {
    // create category tables
    String[] tables = { "beverages", "bakery", "canned", "jared", "dairy", "dry", "frozen", "meat", "produce",
        "cleaners", "paper", "personal" };
    for (int i = 0; i < tables.length; i++) {
      System.out.println("Creating " + tables[i] + " table ...");
      String createProductsTableCmd = "CREATE TABLE IF NOT EXISTS " + tables[i] + " (" +
          "id INT, " +
          "name VARCHAR(255) NOT NULL, " +
          "quantity INT NOT NULL, " +
          "unit_of_measurement INT NOT NULL, " +
          "cost_price DECIMAL(10, 2) NOT NULL, " +
          "selling_price DECIMAL(10, 2) NOT NULL, " +
          "sold INT NOT NULL DEFAULT 0, " +
          "expiry_date DATE NOT NULL, " +
          "created_at DATE NOT NULL, " +
          "vendor_id INT NOT NULL" +
          ")";
      execCreateCommand(createProductsTableCmd);
      System.out.println("Created " + tables[i] + " table");
    }

    // create vendors tables
    String createVendorsTableCmd = "CREATE TABLE IF NOT EXISTS vendors (" +
        "id INT AUTO_INCREMENT PRIMARY KEY, " +
        "name VARCHAR(255) NOT NULL, " +
        "address VARCHAR(255) NOT NULL, " +
        "phone VARCHAR(255) NOT NULL, " +
        "email VARCHAR(255) NOT NULL " +
        ")";
    execCreateCommand(createVendorsTableCmd);

    // create customers tables
    String createCustomersTableCmd = "CREATE TABLE IF NOT EXISTS customers (" +
        "id INT AUTO_INCREMENT PRIMARY KEY, " +
        "name VARCHAR(255) NOT NULL, " +
        "address VARCHAR(255) NOT NULL, " +
        "phone VARCHAR(255) NOT NULL, " +
        "email VARCHAR(255) NOT NULL, " +
        "type VARCHAR(255) NOT NULL " +
        ")";
    execCreateCommand(createCustomersTableCmd);
  }

  private void execCreateCommand(String cmd) {
    try {
      Statement statement = connection.createStatement();
      statement.executeUpdate(cmd);
    } catch (SQLException e) {
      System.err.println("Error creating tables: " + e.getMessage());
    }
  }
}