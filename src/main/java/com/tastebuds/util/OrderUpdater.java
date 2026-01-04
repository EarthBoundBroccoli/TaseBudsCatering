package com.tastebuds.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderUpdater {
    private static final String FILE_NAME = "orders.txt";

    public static void updateOrderStatus(String orderId, String newStatus, String queue, String chefs, String time) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(orderId)) {

                    data[4] = newStatus;
                    data[5] = queue;
                    data[6] = chefs;
                    data[7] = time;
                    line = String.join(",", data);
                }
                lines.add(line);
            }
        } catch (IOException e) { return; }

        // Write everything
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String l : lines) out.println(l);
        } catch (IOException e) { }
    }
}