package com.tastebuds.service;

import com.tastebuds.model.Driver;
import com.tastebuds.model.Order;

public class DeliveryService {

    public void assignDelivery(Order order) {
        if (order.isPriority) {
            // Requirement: Assign vehicle/driver within 10 minutes
            System.out.println("Priority Order: Dispatching vehicle immediately (Target: < 10 mins).");
        } else {
            System.out.println("Normal Order: Dispatching based on availability.");
        }
    }

    public boolean driverCheckout(Order order, Driver driver, String inputLicense) {
        // Requirement: Check out with order number and driving license for security
        if (driver.licenseNumber.equals(inputLicense)) {
            order.status = "In Transit";
            System.out.println("Driver " + driver.name + " checked out Order #" + order.orderId);
            return true;
        } else {
            System.out.println("Security Alert: Invalid License Number!");
            return false;
        }
    }
}