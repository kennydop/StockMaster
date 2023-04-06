package com.stockmaster;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
  private static final DBConnection instance = new DBConnection();
  protected static final String host = "localhost";
  protected static final int port = 3306;
  protected static final String user = "root";
  protected static final String password = "$Root55";
  protected static final String dbName = "stockmaster";
  protected static final String mysqlServerUrl = "jdbc:mysql://" + host + ":" + port;

  private Connection connection;

  private DBConnection() {
  }

  public static DBConnection getInstance() {
    return instance;
  }

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

    } catch (SQLException e) {
      ErrorDialog.showErrorDialog("Error", "Error connecting to MySQL server", e.getMessage());
      System.err.println("Error connecting to MySQL server: " + e.getMessage());
    }
    return connection;
  }

  public void execSQL(String cmd) {
    try {
      PreparedStatement stmt = connection.prepareStatement(cmd);
      stmt.executeUpdate();
    } catch (SQLException e) {
      ErrorDialog.showErrorDialog("Error", "Error Executing SQL", e.getMessage());
      System.err.println("Error executing sql: " + e.getMessage());
    }
  }
}