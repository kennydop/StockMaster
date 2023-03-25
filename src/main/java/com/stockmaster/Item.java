package com.stockmaster;

public class Item {
  private String name;
  private int quantity;
  private double sellingPrice;
  private String vendor;

  public Item(String name, int quantity, double sellingPrice, String vendor) {
    this.name = name;
    this.quantity = quantity;
    this.sellingPrice = sellingPrice;
    this.vendor = vendor;
  }

  // Getters and setters for the fields
  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
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

  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }
}
