package com.sparta.data;

import java.io.*;
import java.util.*;

public class CSVAccessor {
    public static List<String[]> readAllToList(String filePath) {
        List<String[]> list = new ArrayList<>();
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            while ((line = reader.readLine()) != null) {
                list.add(line.split("," ));
            }
        } catch (IOException e) {
            System.err.println("Problem with entry - log me");
        }
        return list;
    }

    public static void writeLinesToCSV(String filePath, LinkedHashMap<Integer, String[]> map) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] entry : map.values()) {
                writer.write(String.join(",", entry));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error Writing to csv");
        }
    }
}
