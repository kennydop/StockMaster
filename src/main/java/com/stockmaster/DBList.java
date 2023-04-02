package com.stockmaster;

import java.sql.*;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBList<T> {
  private Connection connection;
  private int size;
  private String tableName;

  public DBList(String tableName) {
    try {
      this.tableName = tableName;
      this.size = 0;

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
          "created_at TIMESTAMP, " +
          "vendor VARCHAR(255)" +
          ")";
      Statement createTableStatement = connection.createStatement();
      createTableStatement.execute(createTableQuery);

      // Get the current list size
      String countQuery = "SELECT COUNT(*) FROM " + tableName;
      Statement countStatement = connection.createStatement();
      ResultSet countResult = countStatement.executeQuery(countQuery);
      if (countResult.next()) {
        size = countResult.getInt(1);
      }

    } catch (SQLException e) {
      System.err.println("Error connecting to Database: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void add(T x) {
    addItem(x, size);
  }

  public void add(int index, T x) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index + " [" + size + "]");
    }
    addItem(x, index);
  }

  private void addItem(T x, int index) {
    try {
      // Shift all elements to the right of the index
      for (int i = size - 1; i >= index; i--) {
        Item currentItem = (Item) getItem(i);
        setItem(i + 1, currentItem);
      }

      String insertQuery = "";
      if (x.getClass().getName().equals("com.stockmaster.Item")) {
        Item item = (Item) x;
        if (index == size) {
          insertQuery = "INSERT INTO " + tableName
              + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
              + index + ", '" + item.getName() + "', '" + item.getCategory() + "', " + item.getQuantity() + ", '"
              + item.getUnitOfMeasurement() + "', " + item.getCostPrice() + ", " + item.getSellingPrice() + ", "
              + item.getSold() + ", '" + item.getSqlExpiryDate() + "', '" + item.getSqlCreatedAt() + "', '"
              + item.getVendor()
              + "')";
        } else {
          insertQuery = "UPDATE " + tableName + " SET " + item.sqlStr() + " WHERE id = " + index;
        }
      }
      PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
      insertStatement.executeUpdate();
      size++;
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public T get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }
    return getItem(index);
  }

  public T remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }
    T removedItem = getItem(index);
    try {
      // Shift all elements to the left of the index
      for (int i = index; i < size - 1; i++) {
        Item currentItem = (Item) getItem(i + 1);
        setItem(i, currentItem);
      }

      // Remove the last item
      String deleteQuery = "DELETE FROM " + tableName + " WHERE id = " + (size - 1);
      PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
      deleteStatement.executeUpdate();
      size--;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return removedItem;
  }

  public boolean remove(T element) {
    int index = -1;
    for (int i = 0; i < size; i++) {
      if (getItem(i).equals(element)) {
        index = i;
        break;
      }
    }
    if (index != -1) {
      remove(index);
      return true;
    }
    return false;
  }

  public void set(int index, T element) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }
    setItem(index, (Item) element);
  }

  private void setItem(int index, Item item) {
    try {
      String updateQuery = "";
      if (index == size) {
        updateQuery = "INSERT INTO " + tableName
            + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
            + index + ", '" + item.getName() + "', '" + item.getCategory() + "', " + item.getQuantity() + ", '"
            + item.getUnitOfMeasurement() + "', " + item.getCostPrice() + ", " + item.getSellingPrice() + ", "
            + item.getSold() + ", '" + item.getSqlExpiryDate() + "', '" + item.getSqlCreatedAt() + "', '"
            + item.getVendor()
            + "')";
      } else {
        updateQuery = "UPDATE " + tableName + " SET "
            + item.sqlStr() + " WHERE id = " + index;
      }
      PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int size() {
    return size;
  }

  @SuppressWarnings("unchecked")
  private T getItem(int index) {
    T item = null;
    try {
      String selectQuery = "SELECT * FROM " + tableName + " WHERE id = " + index;
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = selectStatement.executeQuery();
      if (resultSet.next()) {
        try {
          item = (T) new Item(
              resultSet.getInt("id"),
              resultSet.getString("name"),
              resultSet.getString("category"),
              resultSet.getInt("quantity"),
              resultSet.getInt("sold"),
              resultSet.getDouble("selling_price"),
              resultSet.getString("vendor"),
              resultSet.getString("unit_of_measurement"),
              resultSet.getDouble("cost_price"),
              resultSet.getTimestamp("expiry_date"),
              resultSet.getDate("created_at"));
        } catch (Exception e) {
          // TODO: handle exception
          e.printStackTrace();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return item;
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