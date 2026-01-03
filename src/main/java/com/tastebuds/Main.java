package com.tastebuds;

import com.tastebuds.service.*;
import com.tastebuds.util.FileHelper;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderService orderService = new OrderService();
        DriverService driverService = new DriverService();
        String currentUser = null;

        while (true) {
            System.out.println("\n============================================");
            System.out.println("||       TASTEBUDS CATERING SYSTEM        ||");
            System.out.println("============================================");
            System.out.println("||  1. Customer Portal                    ||");
            System.out.println("||  2. Head Chef Portal                   ||");
            System.out.println("||  3. Driver Portal                      ||");
            System.out.println("||  4. Exit                               ||");
            System.out.println("============================================");
            System.out.print("Enter your choice: ");
            int portal = scanner.nextInt(); scanner.nextLine();

            if (portal == 1) { // CUSTOMER PORTAL
                System.out.println("\n--------------------------------------------");
                System.out.println("|            CUSTOMER GATEWAY              |");
                System.out.println("--------------------------------------------");
                System.out.println("|  1. Login                                |");
                System.out.println("|  2. Register                             |");
                System.out.println("|  3. Back                                 |");
                System.out.println("--------------------------------------------");
                System.out.print("Enter your choice: ");
                int authChoice = scanner.nextInt(); scanner.nextLine();

                if (authChoice == 2) {
                    System.out.println("\n>>> NEW REGISTRATION");
                    System.out.print("Create Username: "); String u = scanner.nextLine();
                    System.out.print("Create Password: "); String p = scanner.nextLine();

                    if (orderService.registerUser(u, p)) {
                        System.out.println("[SUCCESS] Registration complete! Please login.");
                    } else {
                        System.out.println("[ERROR] Username already exists.");
                    }
                } else if (authChoice == 1) {
                    System.out.print("Username: "); String u = scanner.nextLine();
                    System.out.print("Password: "); String p = scanner.nextLine();
                    if (orderService.loginUser(u, p)) {
                        currentUser = u;
                        System.out.println("\n[SUCCESS] Login Successful! Welcome, " + currentUser);
                    } else {
                        System.out.println("[ERROR] Invalid credentials.");
                        continue;
                    }
                } else continue;

                // LOGGED IN CUSTOMER MENU
                while (currentUser != null) {
                    System.out.println("\n============================================");
                    System.out.println("|| CUSTOMER: " + String.format("%-28s", currentUser) + " ||");
                    System.out.println("============================================");
                    System.out.println("|| 1. Place New Order                     ||");
                    System.out.println("|| 2. View Delivery Status & Feedback     ||");
                    System.out.println("|| 3. Logout                              ||");
                    System.out.println("============================================");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt(); scanner.nextLine();

                    if (choice == 1) {
                        System.out.println("\n--- PLACE NEW ORDER ---");
                        System.out.print("Product Name: "); String prod = scanner.nextLine();
                        System.out.print("Price: "); double price = scanner.nextDouble();

                        double rate = orderService.getDiscountRate(currentUser);
                        double finalPrice = price * (1 - rate);
                        int nextId = orderService.getNextOrderId();

                        System.out.println("--------------------------------------------");
                        System.out.println("Discount Applied: " + (rate * 100) + "%");
                        System.out.println("Final Price: " + finalPrice);
                        System.out.println("--------------------------------------------");

                        String orderData = nextId + "," + currentUser + "," + prod + "," + finalPrice +
                                ",Pending,N/A,N/A,N/A,N/A,N/A,N/A";

                        com.tastebuds.util.FileHelper.logEvent("orders.txt", orderData);
                        System.out.println("[CONFIRMED] Order #" + nextId + " placed successfully!");
                    }
                    else if (choice == 2) {
                        System.out.println("\n--- YOUR ORDER HISTORY ---");
                        java.util.List<String> allOrders = com.tastebuds.util.FileHelper.readFile("orders.txt");
                        boolean hasOrders = false;
                        java.util.List<String> myCheckedOutOrders = new java.util.ArrayList<>();

                        for (String line : allOrders) {
                            if (line.trim().isEmpty()) continue;
                            String[] d = line.split(",");
                            if (d.length > 1 && d[1].equals(currentUser)) {
                                hasOrders = true;
                                System.out.printf("ID: %-4s | Product: %-12s | Total: %-8s | Status: %s\n", d[0], d[2], d[3], d[4]);

                                if (d[4].equalsIgnoreCase("Checked Out") || d[4].equalsIgnoreCase("In Transit")) {
                                    myCheckedOutOrders.add(d[0]);
                                }
                            }
                        }

                        if (!hasOrders) {
                            System.out.println("You have no orders yet.");
                        } else if (!myCheckedOutOrders.isEmpty()) {
                            System.out.println("\n--- FEEDBACK SECTION ---");
                            System.out.print("Enter Order ID to mark Delivered & Rate (or '0' to go back): ");
                            String targetId = scanner.nextLine();

                            if (!targetId.equals("0") && myCheckedOutOrders.contains(targetId)) {
                                System.out.print("Enter Rating (1-5): ");
                                String rating = scanner.nextLine();
                                System.out.print("Enter Suggestions/Comments: ");
                                String comment = scanner.nextLine();

                                for (int i = 0; i < allOrders.size(); i++) {
                                    if (allOrders.get(i).trim().isEmpty()) continue;
                                    String[] data = allOrders.get(i).split(",");
                                    if (data[0].equals(targetId)) {
                                        data[4] = "Delivered"; data[9] = rating; data[10] = comment;
                                        allOrders.set(i, String.join(",", data));
                                        break;
                                    }
                                }
                                com.tastebuds.util.FileHelper.overwriteFile("orders.txt", allOrders);
                                System.out.println("[SUCCESS] Feedback submitted. Order marked as Delivered.");
                            } else if (!targetId.equals("0")) {
                                System.out.println("[ERROR] Invalid ID or Order not ready for feedback.");
                            }
                        } else {
                            System.out.println("\n(No orders currently available for feedback.)");
                        }
                    }
                    else if (choice == 3) { currentUser = null; }
                }
            }
            else if (portal == 2) { // HEAD CHEF PORTAL
                System.out.println("\n--------------------------------------------");
                System.out.println("|             HEAD CHEF PORTAL             |");
                System.out.println("--------------------------------------------");

                java.util.List<String> allOrders = com.tastebuds.util.FileHelper.readFile("orders.txt");
                boolean found = false;

                System.out.println("--- PENDING ORDERS ---");
                for (String line : allOrders) {
                    if (line.trim().isEmpty()) continue;
                    String[] d = line.split(",");
                    if (d.length > 4 && d[4].equals("Pending")) {
                        System.out.println("ID: " + d[0] + " | Client: " + d[1] + " | Product: " + d[2]);
                        found = true;
                    }
                }

                if (!found) {
                    System.out.println("No pending orders available.");
                } else {
                    System.out.print("\nEnter Order ID to Process: ");
                    String targetId = scanner.nextLine();

                    System.out.print("Is this a Priority Order? (yes/no): ");
                    String priorityInput = scanner.nextLine();
                    String status = priorityInput.equalsIgnoreCase("yes") ? "Preparing (Priority)" : "Preparing";

                    System.out.print("Assign Queue Position: ");
                    String q = scanner.nextLine();
                    System.out.print("Number of Chefs assigned: ");
                    String chefs = scanner.nextLine();
                    System.out.print("Estimated Time (mins): ");
                    String est = scanner.nextLine();

                    for (int i = 0; i < allOrders.size(); i++) {
                        if (allOrders.get(i).trim().isEmpty()) continue;
                        String[] data = allOrders.get(i).split(",");
                        if (data[0].equals(targetId)) {
                            data[4] = status; data[5] = q; data[6] = chefs; data[7] = est;
                            allOrders.set(i, String.join(",", data));
                            break;
                        }
                    }
                    com.tastebuds.util.FileHelper.overwriteFile("orders.txt", allOrders);
                    System.out.println("[SUCCESS] Order #" + targetId + " updated to " + status);
                }
            }
            else if (portal == 3) { // DRIVER PORTAL
                System.out.println("\n--------------------------------------------");
                System.out.println("|               DRIVER PORTAL              |");
                System.out.println("--------------------------------------------");
                System.out.println("|  1. Login                                |");
                System.out.println("|  2. Register                             |");
                System.out.println("--------------------------------------------");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt(); scanner.nextLine();

                if (choice == 2) {
                    System.out.println("\n>>> DRIVER REGISTRATION");
                    System.out.print("Name: "); String n = scanner.nextLine();
                    System.out.print("Password: "); String p = scanner.nextLine();
                    System.out.print("License No: "); String lic = scanner.nextLine();
                    System.out.print("Vehicle (Bike/Car/Van): "); String v = scanner.nextLine();
                    driverService.registerDriver(n, p, lic, v);
                    System.out.println("[SUCCESS] Driver Registered successfully!");
                } else {
                    System.out.print("Username: ");
                    String u = scanner.nextLine();
                    System.out.print("Password: ");
                    String p = scanner.nextLine();
                    if (driverService.loginDriver(u, p)) {
                        System.out.println("\n[SUCCESS] Driver Login Successful!");

                        List<String> allOrders = FileHelper.readFile("orders.txt");
                        System.out.println("--- READY FOR DELIVERY ---");

                        boolean ordersAvailable = false;
                        for (String line : allOrders) {
                            if (line.trim().isEmpty()) continue;
                            String[] d = line.split(",");
                            if (d.length >= 5 && d[4].startsWith("Preparing")) {
                                System.out.println("ID: " + d[0] + " | Status: " + d[4]);
                                System.out.println("    INFO: " + driverService.getAssignmentMessage(d[4]));
                                ordersAvailable = true;
                            }
                        }

                        // FIX: Only ask for ID if orders were actually found
                        if (!ordersAvailable) {
                            System.out.println("[INFO] No orders are currently ready for delivery.");
                        } else {
                            System.out.print("\nEnter Order ID to Checkout: ");
                            String targetId = scanner.nextLine();
                            System.out.print("Confirm your License Number: ");
                            String confirmLic = scanner.nextLine();

                            boolean foundTarget = false;
                            for (int i = 0; i < allOrders.size(); i++) {
                                if (allOrders.get(i).trim().isEmpty()) continue;
                                String[] data = allOrders.get(i).split(",");
                                if (data[0].equals(targetId)) {
                                    data[4] = "Checked Out";
                                    data[8] = u;
                                    allOrders.set(i, String.join(",", data));
                                    foundTarget = true;
                                    break;
                                }
                            }

                            if (foundTarget) {
                                com.tastebuds.util.FileHelper.overwriteFile("orders.txt", allOrders);
                                System.out.println("[SUCCESS] Order #" + targetId + " is now Out for Delivery!");
                            } else {
                                System.out.println("[ERROR] Order ID not found or invalid.");
                            }
                        }
                    } else {
                        System.out.println("[ERROR] Invalid Driver credentials.");
                    }
                }
            }
            else if (portal == 4) {
                System.out.println("\n[SYSTEM] Thank you for using TasteBuds. Goodbye!");
                break;
            }
        }
    }
}