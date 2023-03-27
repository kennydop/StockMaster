package com.stockmaster;

public class Customer {
    private String name;
    private String address;
    private String phone;
    private String email;
    private CustomerType type;
    
    public Customer(String name, String address, String phone, String email, CustomerType type) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
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

    public CustomerType getType() {
        return type;
    }
    
    public String getTypeAString() {
        return this.type == CustomerType.Retail ? "Retail" : this.type == CustomerType.Wholesale ? "Wholesale" : "Online";  
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public enum CustomerType {
        Retail,
        Wholesale,
        Online,
    }
}
