package com.tastebuds.model;

public class Customer {
    public String name;
    public boolean isRegistered;
    public int ordersThisMonth;

    public Customer(String name, boolean isRegistered, int ordersThisMonth) {
        this.name = name;
        this.isRegistered = isRegistered;
        this.ordersThisMonth = ordersThisMonth;
    }
}