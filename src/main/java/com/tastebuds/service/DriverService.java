package com.tastebuds.service;

import com.tastebuds.util.FileHelper;
import java.io.*;
import java.util.*;

public class DriverService {
    private static final String DRIVER_FILE = "drivers.txt";

    // Register Driver
    public boolean registerDriver(String name, String pass, String license, String vehicle) {
        String data = name + "," + pass + "," + license + "," + vehicle + ",available";
        FileHelper.logEvent(DRIVER_FILE, data);
        return true;
    }

    // Login for Driver
    public boolean loginDriver(String name, String pass) {
        List<String> lines = FileHelper.readFile(DRIVER_FILE);
        for (String line : lines) {
            String[] d = line.split(",");
            if (d[0].equals(name) && d[1].equals(pass)) return true;
        }
        return false;
    }

    // Assign based on Priority/Normal
    public String getAssignmentMessage(String orderStatus) {
        if (orderStatus.contains("Priority")) {
            return "ALARM: Priority Order! Delivery staff must be assigned within 10 mins.";
        } else {
            return "Standard Order: Assign based on vehicle/driver availability.";
        }
    }
}