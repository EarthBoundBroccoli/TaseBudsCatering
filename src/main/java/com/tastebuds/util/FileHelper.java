package com.tastebuds.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    public static void logEvent(String fileName, String data) {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file, true);
            PrintWriter out = new PrintWriter(fw);
            out.println(data);
            out.flush();
            out.close();
            fw.close();
        } catch (IOException e) {
            System.err.println("FileHelper Error (Write): " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Reading logic
    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("FileHelper Error (Read): " + e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    // Overwriting logic
    public static void overwriteFile(String fileName, List<String> lines) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                out.println(line);
            }
        } catch (IOException e) {
            System.err.println("FileHelper Error (Overwrite): " + e.getMessage());
            e.printStackTrace();
        }
    }
}