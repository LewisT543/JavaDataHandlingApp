package com.sparta.data.models.utils;

import java.io.*;
import java.util.*;

public class CSVAccessor {
    private static ArrayList<String[]> duplicates = new ArrayList<>();
    public static List<String[]> readCSVToList(String filePath) {
        ArrayList<String[]> validLines = new ArrayList<>();
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

    public static ArrayList<String[]> getDuplicates() {
        return duplicates;
    }

//    public static void writeArrLstStrToCSV(String filePath, ArrayList<String[]> arr) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (String[] entry : arr) {
//                writer.write(String.join(",", entry));
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            System.err.println("Error Writing to csv");
//        }
//    }
//
//    public static void writeLHMToCSV(String filePath, LinkedHashMap<Integer, Employee> map) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (Map.Entry<Integer, Employee> entry : map.entrySet()) {
//                writer.write(entry.getValue().toString());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            System.err.println("Error Writing to csv");
//        }
//    }
//
//    public static void printDuplicates() {
//        for (String[] line : duplicates) {
//            System.out.println(Arrays.toString(line));
//        }
//    }


}
