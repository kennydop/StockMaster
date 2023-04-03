package com.stockmaster;

import java.time.LocalDate;
import java.util.Date;

public class Bill {
  private int id;
  private int invoiceNumber;
  private String issuedTo;
  private Date issuedDate;
  private Double amountPayed;

  public Bill(int id, int invoiceNumber, String issuedTo, Date issuedDate, Double amountPayed) {
    this.id = id;
    this.invoiceNumber = invoiceNumber;
    this.issuedTo = issuedTo;
    this.issuedDate = issuedDate;
    this.amountPayed = amountPayed;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(int invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String getIssuedTo() {
    return issuedTo;
  }

  public void setIssuedItem(String issuedTo) {
    this.issuedTo = issuedTo;
  }

  public Date getLocalDate() {
    return new java.sql.Date(this.issuedDate.getTime());
  }

  public Date getIssuedDate() {
    return issuedDate;
  }

  public Double getAmountPayed() {
    return amountPayed;
  }

  public void setAmountPayed(Double amountPayed) {
    this.amountPayed = amountPayed;
  }

  @Override
  public String toString() {
    return "Bill{" +
        "invoiceNumber=" + invoiceNumber +
        ", issuedTo=" + issuedTo +
        ", amountPayed=" + amountPayed +
        ", date=" + issuedDate +
        '}';
  }

  public String sqlStr() {
    java.sql.Date issuedSqlDate = new java.sql.Date(issuedDate.getTime());
    return "invoice_number = " + invoiceNumber + "issued_to = " + issuedTo + "amount_payed = " + amountPayed
        + ", issued_date = '"
        + issuedSqlDate + "'";
  }

}
