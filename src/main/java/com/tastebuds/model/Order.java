package com.tastebuds.model;

public class Order {
    public int orderId;
    public int queueNumber;
    public String details;
    public boolean isPriority; // Head Chef decides this
    public String status;      // e.g., "Pending", "Preparing", "Ready", "Delivered"

    public Order(int orderId, int queueNumber, String details) {
        this.orderId = orderId;
        this.queueNumber = queueNumber;
        this.details = details;
        this.status = "Pending";
    }
}