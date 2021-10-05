package com.sparta.data;

import java.io.IOException;

// Callable statement seems to be the fastest writing method, provided I can create procedures in my DB.

public class Manager {
    public static void main(String[] args) throws IOException {
        parseEmployeeObjects("EmployeeRecordsLarge.csv", "rejects.csv");
    }

    public static void parseEmployeeObjects (String inPath, String outPath) {
        DataHandler handler = new DataHandler();
        handler.readAllToObjects(inPath, "e");
        CSVAccessor.writeLinesToCSV(outPath, handler.getDuplicates());
    }
}
