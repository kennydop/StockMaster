package com.stockmaster;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBList<T> {
  private Connection connection;
  private int size;
  private String tableName;
  private Class<T> clazz;

  public DBList(String tableName, Class<T> clazz) {
    try {
      this.tableName = tableName;
      this.size = 0;
      this.clazz = clazz;

      // Connect to the database
      this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBConnection.dbName,
          DBConnection.user,
          DBConnection.password);

      // Create the table if it doesn't exist
      String createTableQuery = "";
      if (clazz.equals(Item.class)) {
        createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
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
      } else if (clazz.equals(IssuedItem.class)) {
        createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "id INT UNIQUE, " +
            "name VARCHAR(255), " +
            "issued_to VARCHAR(255), " +
            "quantity INT, " +
            "unit_cost DECIMAL(10, 2), " +
            "issued_date DATE, " +
            "category VARCHAR(255)" +
            ")";
      } else if (clazz.equals(Bill.class)) {
        createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
            "id INT UNIQUE, " +
            "invoice_code VARCHAR(255), " +
            "issued_to VARCHAR(255), " +
            "amount_payed DECIMAL(10, 2), " +
            "issued_date DATE " +
            ")";
      }
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
        T currentItem = getItem(i);
        setItem(i + 1, currentItem);
      }

      String insertQuery = "";
      if (clazz.equals(Item.class)) {
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
      } else if (clazz.equals(IssuedItem.class)) {
        IssuedItem item = (IssuedItem) x;
        if (index == size) {
          insertQuery = "INSERT INTO " + tableName
              + " (id, name, issued_to, quantity, unit_cost, issued_date, category) VALUES ("
              + index + ", '" + item.getName() + "', '" + item.getIssuedTo() + "', " + item.getQuantity() + ", "
              + item.getUnitCost() + ", '" + item.getSqlDate() + "', '" + item.getCategory()
              + "')";
        } else {
          insertQuery = "UPDATE " + tableName + " SET " + item.sqlStr() + " WHERE id = " + index;
        }
      } else if (clazz.equals(Bill.class)) {
        Bill bill = (Bill) x;
        if (index == size) {
          insertQuery = "INSERT INTO " + tableName
              + " (id, issued_to, invoice_code, amount_payed, issued_date) VALUES ("
              + index + ", '" + bill.getIssuedTo() + "', '" + bill.getInvoiceNumber() + "', "
              + bill.getAmountPayed() + ", '"
              + bill.getLocalDate() + "')";
        } else {
          insertQuery = "UPDATE " + tableName + " SET " + bill.sqlStr() + " WHERE id = " + index;
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
        T currentItem = getItem(i + 1);
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
    setItem(index, element);
  }

  private void setItem(int index, T item) {
    try {
      String updateQuery = "";
      if (clazz.equals(Item.class)) {
        Item x = (Item) item;
        if (index == size) {
          updateQuery = "INSERT INTO " + tableName
              + " (id, name, category, quantity, unit_of_measurement, cost_price, selling_price, sold, expiry_date, created_at, vendor) VALUES ("
              + index + ", '" + x.getName() + "', '" + x.getCategory() + "', " + x.getQuantity() + ", '"
              + x.getUnitOfMeasurement() + "', " + x.getCostPrice() + ", " + x.getSellingPrice() + ", "
              + x.getSold() + ", '" + x.getSqlExpiryDate() + "', '" + x.getSqlCreatedAt() + "', '"
              + x.getVendor()
              + "')";
        } else {
          updateQuery = "UPDATE " + tableName + " SET "
              + x.sqlStr() + " WHERE id = " + index;
        }
      } else if (clazz.equals(IssuedItem.class)) {
        IssuedItem x = (IssuedItem) item;
        if (index == size) {
          updateQuery = "INSERT INTO " + tableName
              + " (id, name, issued_to, quantity, unit_cost, issued_date, category) VALUES ("
              + index + ", '" + x.getName() + "', '" + x.getIssuedTo() + "', " + x.getQuantity() + ", "
              + x.getUnitCost() + ", '" + x.getSqlDate() + "', '" + x.getCategory()
              + "')";
        } else {
          updateQuery = "UPDATE " + tableName + " SET "
              + x.sqlStr() + " WHERE id = " + index;
        }
      } else if (clazz.equals(Bill.class)) {
        Bill x = (Bill) item;
        if (index == size) {
          updateQuery = "INSERT INTO " + tableName
              + " (id, issued_to, invoice_code, amount_payed, issued_date) VALUES ("
              + index + ", '" + x.getIssuedTo() + "', '" + x.getInvoiceNumber() + "', " + x.getAmountPayed() + ", '"
              + x.getLocalDate() + "')";
        } else {
          updateQuery = "UPDATE " + tableName + " SET "
              + x.sqlStr() + " WHERE id = " + index;
        }
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
          if (clazz.equals(Item.class)) {
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
                resultSet.getDate("expiry_date"),
                resultSet.getDate("created_at"));
          } else if (clazz.equals(IssuedItem.class)) {
            item = (T) new IssuedItem(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("issued_to"),
                resultSet.getDate("issued_date"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("unit_cost"),
                resultSet.getString("category"));
          } else if (clazz.equals(Bill.class)) {
            item = (T) new Bill(
                resultSet.getInt("id"),
                resultSet.getString("invoice_code"),
                resultSet.getString("issued_to"),
                resultSet.getDate("issued_date"),
                resultSet.getDouble("amount_payed"));
          }

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

  public ObservableList<T> getItems() {
    ObservableList<T> items = FXCollections.observableArrayList();
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
      if (clazz.equals(Item.class)) {
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
            items.add((T) item);
          }
        }
      } else if (clazz.equals(IssuedItem.class)) {
        while (resultSet.next()) {
          int id = resultSet.getInt("id");
          String name = resultSet.getString("name");
          String issued_to = resultSet.getString("issued_to");
          int quantity = resultSet.getInt("quantity");
          double unit_cost = resultSet.getDouble("unit_cost");
          Date issued_date = resultSet.getDate("issued_date");
          String category = resultSet.getString("category");
          if (name != null) {
            IssuedItem item = new IssuedItem(id, name, issued_to, issued_date, quantity, unit_cost, category);
            items.add((T) item);
          }
        }
      } else if (clazz.equals(Bill.class)) {
        Set<String> invoice_codes = new HashSet<String>();
        while (resultSet.next()) {
          int id = resultSet.getInt("id");
          String invoice_code = resultSet.getString("invoice_code");
          String issued_to = resultSet.getString("issued_to");
          Date issued_date = resultSet.getDate("issued_date");
          Double amount_payed = resultSet.getDouble("amount_payed");

          Bill bill = new Bill(id, invoice_code, issued_to, issued_date, amount_payed);
          items.add((T) bill);
          invoice_codes.add(invoice_code);
        }
        UniqueRandomCodeGenerator.setGeneratedCodes(invoice_codes);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return items;
  }

  public Class<T> getGenericType() {
    return clazz;
  }
}