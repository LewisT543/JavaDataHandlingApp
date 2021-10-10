package com.sparta.data.controller;

import com.sparta.data.models.utils.DataHandler;
import com.sparta.data.models.utils.JDBCDriver;
import com.sparta.data.views.DataHandlerView;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;

public class Controller {
    private final DataHandler handler;
    private final Logger logger;
    private final LinkedHashMap<String, String> CSV_CHOICES = new LinkedHashMap<>() {{
        put("s", "EmployeeRecords.csv");
        put("l", "EmployeeRecordsLarge.csv");
        put("x", "Exit program");
    }};
    private final LinkedHashMap<String, String> ROUTE_CHOICES = new LinkedHashMap<>() {{
        put("o", "Object Oriented route (slow)");
        put("f", "Functional route (very fast)");
    }};
    private final LinkedHashMap<String, String> DATABASE_CHOICES = new LinkedHashMap<>() {{
        put("l", "SQLite");
        put("m", "MySQL");
    }};
    private final LinkedHashMap<String, String> DATABASE_CONNECTIONS = new LinkedHashMap<>() {{
        put("l", "jdbc:sqlite:employees.db");
        put("m", "jdbc:mysql://localhost:3306/employees?rewriteBatchedStatements=true");
    }};
    private final LinkedHashMap<String, String> DB_WRITE_METHOD = new LinkedHashMap<>() {{
        put("b", "Batch Processing");
        put("m", "MultiThreading (slightly faster)");
    }};

    public Controller(DataHandler handler, Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public void readAndWrite() {
        DataHandlerView.printWelcomeBanner();
        String choice = DataHandlerView.getInput(CSV_CHOICES, "a file to read from.");
        System.out.println("------ Reading data from CSV ------");
        // Small CSVFile
        if (choice.equals("s")) {
            readSmallCSV(choice);
        // Large CSVFile
        } else {
            String dbChoice = DataHandlerView.getInput(DATABASE_CHOICES, "a database to write to.");
            DataHandlerView.displayInitialisationResults(JDBCDriver.initialiseDb(DATABASE_CONNECTIONS.get(dbChoice)));
            String route = DataHandlerView.getInput(ROUTE_CHOICES, "a route preference.");
            // OOP route (slow)
            if (route.equals("o")) {
                DataHandlerView.displayReadResults(handler.readFromCSVToEmployees(CSV_CHOICES.get(choice)));
                handler.employeesToWriteableEmployees(handler.getEmployeesArr());
            // Functional route (very fast)
            } else {
                DataHandlerView.displayReadResults(handler.functionalReadFromCSVToWriteableEmployees(CSV_CHOICES.get(choice)));
            }
            // Write to SQLite DB
            if (dbChoice.equals("l")) {
                System.out.println("------ Writing to Database, please wait... ------");
                batch1000Insert(dbChoice);
            // Write to MySQL DB
            } else {
                String writeChoice = DataHandlerView.getInput(DB_WRITE_METHOD, "a method to write to database.");
                // MultiThreaded write
                if (writeChoice.equals("m")) {
                    threadInsert();
                // Regular batch writing
                } else {
                    batch1000Insert(dbChoice);
                }
            }
        }
    }
    public void readSmallCSV(String choice) {
        String[] readStats = handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
        DataHandlerView.displayReadResults(readStats);
        logger.info("Reading stats: [#rows:" + readStats[0] + ", #rejects:" + readStats[1] + ", #readTime:"
                + readStats[2] + ", #createTime:" + readStats[3] + "]");
    }

    public void batch1000Insert(String dbChoice) {
        String[] writeStats = JDBCDriver.insertAllBatchesOf1000(
                handler.getWriteableEmployeesArr(), DATABASE_CONNECTIONS.get(dbChoice));
        DataHandlerView.displayInsertResults(writeStats);
        logger.info("Writing stats: [#rows:" + writeStats[0] + ", timeTaken:" + writeStats[1] + "]");
    }

    public void threadInsert() {
        int threads = DataHandlerView.getIntegerInput(2, 100);
        String[] writeStats = JDBCDriver.threadedInsert(handler.getWriteableEmployeesArr(), threads);
        DataHandlerView.displayInsertResults(writeStats);
        logger.info("Writing stats: [#threads:" + threads + ", #rows:" + writeStats[0] + ", timeTaken:" + writeStats[1] + "]");
    }
}

