package com.stockmaster;

import java.sql.*;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBQueue<T> {
  private Connection connection;
  private int front;
  private int rear;
  private int capacity;
  private String tableName;

  public DBQueue(int size, String tableName) {
    try {
      this.capacity = size;
      this.front = -1;
      this.rear = -1;
      this.tableName = tableName;

      // Connect to the database
      this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBConnection.dbName,
          DBConnection.user,
          DBConnection.password);

      // Create the table if it doesn't exist
      String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
          "id INT UNIQUE, " +
          "name VARCHAR(255), " +
          "category VARCHAR(255), " +
          "quantity INT, " +
          "unit_of_measurement VARCHAR(255), " +
          "cost_price DECIMAL(10, 2), " +
          "selling_price DECIMAL(10, 2), " +
          "sold INT, " +
          "expiry_date DATE, " +
          "created_at DATE, " +
          "vendor VARCHAR(255)" +
          ")";
      Statement createTableStatement = connection.createStatement();
      createTableStatement.execute(createTableQuery);

      // Get the current queue size
      int _sizeOfTable = 0;
      String countQuery = "SELECT COUNT(*) FROM " + tableName;
      Statement countStatement = connection.createStatement();
      ResultSet countResult = countStatement.executeQuery(countQuery);
      if (countResult.next()) {
        _sizeOfTable = countResult.getInt(1);
        if (_sizeOfTable == 0) {
          capacity = size;
        }
      }
      // Get the top index
      String rearSelectionQuery = "SELECT * FROM " + tableName;
      Statement rearSelectionStatement = connection.createStatement();
      ResultSet rearSelectionResult = rearSelectionStatement.executeQuery(rearSelectionQuery);
      while (rearSelectionResult.next()) {
        if (rearSelectionResult.getString("name") != null) {
          rear++;
        } else {
          break;
        }
      }
      if (rear > -1) {
        front = 0;
      }

      // If the queue is empty, fill it with null data
      if (_sizeOfTable == 0) {
        System.out.println("initializing " + tableName + " with default NULL values...");
        for (int i = 0; i < capacity; i++) {
          String insertQuery = "INSERT INTO " + tableName
              + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
              + i + ", NULL, '" + tableName + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
          Statement insertStatement = connection.createStatement();
          insertStatement.execute(insertQuery);
        }

        System.out.println(tableName + " initiated wil NULL values");
      }

    } catch (SQLException e) {
      System.err.println("Error connecting to Database: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void enqueue(T x) {
    if (isFull()) {
      expandQueueSize();
    }
    String updateQuery = "";
    if (x.getClass().getName().equals("com.stockmaster.Item")) {
      Item item = (Item) x;
      updateQuery = "UPDATE " + tableName + " SET " + item.sqlStr() + " WHERE id = " + (rear + 1);
    }
    rear += 1;
    try {
      PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public T dequeue() {
    if (isEmpty()) {
      System.out.println("QUEUE EMPTY");
      System.exit(1);
    }

    T dequeuedItem = null;
    try {
      System.out.println(tableName + " front: " + front + " rear: " + rear);
      String selectQuery = "SELECT * FROM " + tableName + " WHERE id = " + front;
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet result = selectStatement.executeQuery();
      if (result.next()) {
        // dequeuedItem = new Item(result.getInt(1), result.getString());
        // set row to null
        String updateQuery = "UPDATE " + tableName + " SET "
            + "name = NULL, quantity = NULL, unit_of_measurement = NULL, cost_price = NULL, selling_price = NULL, sold = NULL, expiry_date = NULL, created_at = NULL, vendor = NULL WHERE id = "
            + front;
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.executeUpdate();
        shiftRows();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return dequeuedItem;
  }

  private void expandQueueSize() {
    try {
      for (int i = capacity; i < capacity * 2; i++) {
        String insertQuery = "INSERT INTO " + tableName
            + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
            + i + ", NULL, '" + tableName + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
        Statement insertStatement = connection.createStatement();
        insertStatement.execute(insertQuery);
      }
      capacity *= 2;
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println("Expansion failed: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void shiftRows() {
    try {
      // select all rows from the table
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
      resultSet.next();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String category = resultSet.getString("category");
        int quantity = resultSet.getInt("quantity");
        String unit_of_measurement = resultSet.getString("unit_of_measurement");
        double cost_price = resultSet.getDouble("cost_price");
        double selling_price = resultSet.getDouble("selling_price");
        int sold = resultSet.getInt("sold");
        Date expiry_date = resultSet.getDate("expiry_date");
        Date created_at = resultSet.getDate("created_at");
        String vendor = resultSet.getString("vendor");
        Item item = new Item(id, name, category, quantity, sold, selling_price, vendor, unit_of_measurement,
            cost_price,
            expiry_date, created_at);
        if (name == null) {
          break;
        }
        // shift row up by one
        String shiftQuery = "UPDATE " + tableName + " SET "
            + item.sqlStr() + " WHERE id = "
            + (id - 1);
        PreparedStatement shiftStatement = connection.prepareStatement(shiftQuery);
        shiftStatement.executeUpdate();
      }

      String rearShiftQuery = "UPDATE " + tableName + " SET "
          + "name = NULL, quantity = NULL, unit_of_measurement = NULL, cost_price = NULL, selling_price = NULL, sold = NULL, expiry_date = NULL, created_at = NULL, vendor = NULL WHERE id = "
          + rear;
      PreparedStatement rearShiftStatement = connection.prepareStatement(rearShiftQuery);
      rearShiftStatement.executeUpdate();
      rear -= 1;
    } catch (Exception e) {
      System.out.println("An error occurred while shifting rows:");
      e.printStackTrace();
    }
  }

  public int getSize() {
    return capacity;
  }

  public Boolean isEmpty() {
    return rear == -1 && front == -1;
  }

  public Boolean isFull() {
    return rear == capacity - 1;
  }

  public ObservableList<Item> getItems() {
    ObservableList<Item> items = FXCollections.observableArrayList();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String category = resultSet.getString("category");
        int quantity = resultSet.getInt("quantity");
        String unit_of_measurement = resultSet.getString("unit_of_measurement");
        double cost_price = resultSet.getDouble("cost_price");
        double selling_price = resultSet.getDouble("selling_price");
        int sold = resultSet.getInt("sold");
        Date expiry_date = resultSet.getDate("expiry_date");
        Date created_at = resultSet.getDate("created_at");
        String vendor = resultSet.getString("vendor");
        if (name != null) {
          Item item = new Item(id, name, category, quantity, sold, selling_price, vendor, unit_of_measurement,
              cost_price,
              expiry_date, created_at);
          items.add(item);
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return items;
  }
}
