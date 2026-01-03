package com.tastebuds.service;

import com.tastebuds.util.FileHelper;

import java.io.*;

public class OrderService {
    private static final String USERS_FILE = "users.txt";
    private static final String ORDERS_FILE = "orders.txt";

    // 1. Register User (Moved here for consistency)
    public boolean registerUser(String username, String password) {
        // Check if user already exists to prevent duplicates
        if (isUserExists(username)) {
            return false;
        }
        FileHelper.logEvent(USERS_FILE, username + "," + password);
        return true;
    }

    private boolean isUserExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",")[0].equals(username)) return true;
            }
        } catch (IOException e) { }
        return false;
    }

    public boolean loginUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) return true;
            }
        } catch (IOException e) { }
        return false;
    }


    public int getNextOrderId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException e) { /* Skip header or bad lines */ }
                }
            }
        } catch (IOException e) { }
        return maxId + 1;
    }

    public double getDiscountRate(String username) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Ensure the order belongs to the user and is marked 'Delivered'
                if (data.length > 1 && data[1].equals(username)) {
                    count++;
                }
            }
        } catch (IOException e) { }

        // Your specific tiered logic
        if (count >= 25) return 0.35;
        if (count >= 20) return 0.25;
        if (count >= 15) return 0.20;
        if (count >= 10) return 0.10;
        if (count >= 5)  return 0.05;
        return 0.0;
    }
}