package com.sparta.data;

import java.io.*;
import java.util.*;

public class CSVAccessor {
    private static ArrayList<String[]> duplicates = new ArrayList<>();
    public static List<String[]> readAllToList(String filePath) {
        List<String[]> validLines = new ArrayList<>();
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            ArrayList<String> ids = new ArrayList<>();
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] myLine = line.split("," );
                if (!ids.contains(myLine[0])) {
                    ids.add(myLine[0]);
                    validLines.add(myLine);
                } else {
                    duplicates.add(myLine);
                }
            }
        } catch (IOException e) {
            System.err.println("Problem with entry - log me");
        }
        return validLines;
    }

    public static void writeLinesToCSV(String filePath, ArrayList<String[]> arr) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] entry : arr) {
                writer.write(String.join(",", entry));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error Writing to csv");
        }
    }

    public static void printDuplicates() {
        for (String[] line : duplicates) {
            System.out.println(Arrays.toString(line));
        }
    }

    public static ArrayList<String[]> getDuplicates() {
        return duplicates;
    }
}
