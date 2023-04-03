package com.stockmaster;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Item {
  private int id = -1;
  private String name;
  private String category;
  private int quantity;
  private int sold = 0;
  private double sellingPrice;
  private String vendor;
  private String unitOfMeasurement;
  private double costPrice;
  private Date expiryDate;
  private Date createdAt;

  public Item(int id, String name, String category, int quantity, int sold, double sellingPrice,
      String vendor,
      String unitOfMeasurement, double costPrice, Date expiryDate, Date createdAt) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.quantity = quantity;
    this.sold = sold;
    this.sellingPrice = sellingPrice;
    this.vendor = vendor;
    this.unitOfMeasurement = unitOfMeasurement;
    this.costPrice = costPrice;
    this.expiryDate = expiryDate;
    this.createdAt = createdAt;
  }

  // Getters and setters for the fields
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public int getSold() {
    return sold;
  }

  public double getSellingPrice() {
    return sellingPrice;
  }

  public String getVendor() {
    return vendor;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setSold(int sold) {
    this.sold = sold;
  }

  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public Date getSqlExpiryDate() {
    return new java.sql.Date(expiryDate.getTime());
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void setExpiryDate(LocalDate value) {
    this.expiryDate = Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public String getUnitOfMeasurement() {
    return unitOfMeasurement;
  }

  public void setUnitOfMeasurement(String unitOfMeasurement) {
    this.unitOfMeasurement = unitOfMeasurement;
  }

  public double getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(double costPrice) {
    this.costPrice = costPrice;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getSqlCreatedAt() {
    return new java.sql.Date(createdAt.getTime());
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public String toString() {
    return "id = " + id + ", name = " + name + ", category = " + category + ", quantity = " + quantity + ", sold = "
        + sold
        + ", sellingPrice = " + sellingPrice + ", vendor = "
        + vendor + ", unitOfMeasurement = " + unitOfMeasurement + ", costPrice = " + costPrice + ", expiryDate = "
        + expiryDate + ", createdAt = " + createdAt;
  }

  public String sqlStr() {
    java.sql.Date exp = new java.sql.Date(expiryDate.getTime());
    java.sql.Date ct = new java.sql.Date(createdAt.getTime());
    return "name = '" + name + "', category = '" + category + "', quantity = " + quantity + ", sold = "
        + sold
        + ", selling_price = " + sellingPrice + ", vendor = '"
        + vendor + "', unit_of_measurement = '" + unitOfMeasurement + "', cost_price = " + costPrice
        + ", expiry_date = '"
        + exp + "', created_at = '" + ct + "'";
  }

  public static Item nullItem() {
    return new Item(-1, null, null, -1, -1, -1, null, null, -1, null, null);
  }

  public boolean isNull() {
    return name == null || quantity < 0 || category == null || sold == -1 || sellingPrice < 0
        || costPrice < 0 || unitOfMeasurement == null || vendor == null || expiryDate == null || createdAt == null;
  }

}
