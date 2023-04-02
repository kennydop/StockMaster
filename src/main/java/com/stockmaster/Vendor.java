package com.stockmaster;

import java.util.HashMap;

public class Vendor {
  private String name;
  private String address;
  private String phone;
  private String email;

  public Vendor(String name, String address, String phone, String email) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public static Vendor nullVendor() {
    return new Vendor(null, null, null, null);
  }

  public boolean isNull() {
    return name == null || address == null || phone == null || email == null;
  }

  public HashMap<String, String> toHashMap() {
    HashMap<String, String> hashMappedVendor = new HashMap<String, String>();
    hashMappedVendor.put("name", name);
    hashMappedVendor.put("address", address);
    hashMappedVendor.put("email", email);
    hashMappedVendor.put("phone", phone);
    return hashMappedVendor;
  }

  @Override
  public String toString() {
    return "Vendor{" +
        "name -> '" + name + '\'' +
        ", address -> '" + address + '\'' +
        ", phone -> '" + phone + '\'' +
        ", email -> '" + email + '\'' +
        '}';
  }
}
