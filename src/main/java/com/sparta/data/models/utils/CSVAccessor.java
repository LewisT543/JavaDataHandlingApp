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
            e.printStackTrace();
        }
        return validLines;
    }

    public static ArrayList<String[]> getDuplicates() {
        return duplicates;
    }
}
