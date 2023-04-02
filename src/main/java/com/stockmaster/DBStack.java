package com.stockmaster;

import java.sql.*;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBStack<T> {
  private Connection connection;
  private int top;
  private int capacity;
  private String tableName;

  public DBStack(int size, String tableName) {
    try {
      this.capacity = size;
      this.top = -1;
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
          "expiry_date TIMESTAMP, " +
          "created_at DATE, " +
          "vendor VARCHAR(255)" +
          ")";
      Statement createTableStatement = connection.createStatement();
      createTableStatement.execute(createTableQuery);

      // Get the current stack size
      int _sizeOfTable = 0;
      String countQuery = "SELECT COUNT(*) FROM " + tableName;
      Statement countStatement = connection.createStatement();
      ResultSet countResult = countStatement.executeQuery(countQuery);
      if (countResult.next()) {
        _sizeOfTable = countResult.getInt(1);
        if (_sizeOfTable <= 0) {
          capacity = size;
        }
      }

      // Get the top index
      String topSelectionQuery = "SELECT * FROM " + tableName;
      Statement topSelectionStatement = connection.createStatement();
      ResultSet topSelectionResult = topSelectionStatement.executeQuery(topSelectionQuery);
      while (topSelectionResult.next()) {
        if (topSelectionResult.getString("name") == null) {
          top++;
        } else {
          top++;
          break;
        }
      }
      if (isEmpty()) {
        top = -1;
      }

      // If the stack is empty, fill it with null data
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

  public void push(T x) {
    if (isFull()) {
      expandStackSize();
    }
    String updateQuery = "";
    if (x.getClass().getName().equals("com.stockmaster.Item")) {
      Item item = (Item) x;
      updateQuery = "UPDATE " + tableName + " SET " + item.sqlStr() + " WHERE id = " + (top - 1);
    }
    top -= 1;
    try {
      PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public T pop() {
    if (isEmpty()) {
      System.out.println("STACK EMPTY");
      System.exit(1);
    }

    T poppedItem = null;
    String selectQuery = "SELECT * FROM " + tableName + " WHERE id = " + top;
    try {
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet result = selectStatement.executeQuery();
      if (result.next()) {
        // poppedItem = new Item(result.getInt(1), result.getString());
        // set row to null
        String updateQuery = "UPDATE " + tableName + " SET "
            + "name = NULL, quantity = NULL, unit_of_measurement = NULL, cost_price = NULL, selling_price = NULL, sold = NULL, expiry_date = NULL, created_at = NULL, vendor = NULL WHERE id = "
            + top;
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.executeUpdate();
        top += 1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return poppedItem;
  }

  private void expandStackSize() {
    try {
      String selectQuery = "SELECT * FROM " + tableName;
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet result = selectStatement.executeQuery();
      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("name");
        String category = result.getString("category");
        int quantity = result.getInt("quantity");
        String unit_of_measurement = result.getString("unit_of_measurement");
        double cost_price = result.getDouble("cost_price");
        double selling_price = result.getDouble("selling_price");
        int sold = result.getInt("sold");
        Date expiry_date = result.getTimestamp("expiry_date");
        Date created_at = result.getDate("created_at");
        String vendor = result.getString("vendor");

        // insert at new index after expand
        String insertQuery = "INSERT INTO " + tableName
            + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
            + (capacity + id) + ", '" + name + "', '"
            + category + "', " + quantity + ", '" + unit_of_measurement + "', " + cost_price + ", "
            + selling_price + ", " + sold + ", '" + expiry_date + "', '" + created_at + "', '" + vendor + "')";
        Statement insertStatement = connection.createStatement();
        insertStatement.execute(insertQuery);

        // set the old index to null
        String nullifyRowQuery = "UPDATE " + tableName + " SET " +
            "name = NULL, quantity = NULL, unit_of_measurement = NULL, cost_price = NULL, selling_price = NULL, sold = NULL, expiry_date = NULL, created_at = NULL, vendor = NULL WHERE id = "
            + id;
        Statement nullifyStatement = connection.createStatement();
        nullifyStatement.execute(nullifyRowQuery);
      }

      top = capacity;
      capacity *= 2;
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println("Expansion failed: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public int getSize() {
    return capacity;
  }

  public Boolean isEmpty() {
    return top == -1 || top == capacity - 1;
  }

  public Boolean isFull() {
    return top == 0;
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
        Date expiry_date = resultSet.getTimestamp("expiry_date");
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
