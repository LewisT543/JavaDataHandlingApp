package com.sparta.data;

import java.io.IOException;

public class Manager {
    public static void main(String[] args) throws IOException {
        parseEmployeeObjects("EmployeeRecords.csv", "rejects.csv");
    }

    public static void parseEmployeeObjects (String inPath, String outPath) {
        DataHandler handler = new DataHandler();
        handler.readAllToObjects(inPath);
        CSVAccessor.writeLinesToCSV(outPath, handler.getRejects());
    }
}
