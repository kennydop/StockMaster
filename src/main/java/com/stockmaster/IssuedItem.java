package com.stockmaster;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class IssuedItem {
  private int id = -1;
  private String name;
  private String issuedTo;
  private int quantity;
  private double unitCost;
  private Date issuedDate;
  private LocalDate issuedLocalDate;
  private String category;

  public IssuedItem(int id, String name, String issuedTo, Date issuedDate, int quantity, double unitCost,
      String category) {
    this.id = id;
    this.name = name;
    this.issuedTo = issuedTo;
    this.issuedDate = issuedDate;
    this.quantity = quantity;
    this.unitCost = unitCost;
    this.category = category;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setIssuedDate(Date date) {
    this.issuedDate = date;
  }

  public void setIssuedDate(LocalDate date) {
    this.issuedDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public Date getIssuedDate() {
    return this.issuedDate;
  }

  public LocalDate getIssuedLocalDate() {
    return this.issuedDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }

  public java.sql.Date getSqlDate() {
    return new java.sql.Date(this.issuedDate.getTime());
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setIssuedTo(String issuedTo) {
    this.issuedTo = issuedTo;
  }

  public String getIssuedTo() {
    return this.issuedTo;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setUnitCost(double unitCost) {
    this.unitCost = unitCost;
  }

  public double getUnitCost() {
    return this.unitCost;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public static IssuedItem nullItem() {
    return new IssuedItem(-1, null, null, null, -1, -1, null);
  }

  public boolean isNull() {
    return name == null || quantity < 0 || issuedDate == null || unitCost == -1 || issuedTo == null;
  }

  public String sqlStr() {
    java.sql.Date isd = new java.sql.Date(issuedDate.getTime());
    return "name = '" + name + "', category = '" + category + "', quantity = " + quantity + ", unit_cost = " + unitCost
        + ", issued_to = '" + issuedTo + "', issued_date = '" + isd + "'";
  }

}
