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
        put("o", "Object Oriented route");
        put("f", "Functional route");
    }};
    private final LinkedHashMap<String, String> DATABASE_CHOICES = new LinkedHashMap<>() {{
        put("l", "SQLite");
        put("m", "MySQL");
    }};
    private final LinkedHashMap<String, String> DATABASE_CONNECTIONS = new LinkedHashMap<>() {{
        put("l", "jdbc:sqlite:employees.db");
        // ADD MYSQL CONNECTION HERE
    }};

    public Controller(DataHandler handler, Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public void readAndWrite() {
        // Which CSV file?
        String choice = DataHandlerView.getInput(CSV_CHOICES, "a file to read from.");
        System.out.println("------ Reading data from CSV ------");
        String[] readStats;
        // if (file = s)
        if (choice.equals("s")) {
            readStats = handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
            DataHandlerView.displayReadResults(readStats);
            logger.info("Reading stats: [#rows:" + readStats[0] + ", #rejects:" + readStats[1] + ", #readTime:"
                    + readStats[2] + ", #createTime:" + readStats[3] + "]");
        }
        else {
            String dbChoice = DataHandlerView.getInput(DATABASE_CHOICES, "a database to write to.");
            DataHandlerView.displayInitialisationResults(JDBCDriver.initialiseDb(DATABASE_CONNECTIONS.get(dbChoice)));
            String route = DataHandlerView.getInput(ROUTE_CHOICES, "a route preference.");
            if (route.equals("o")) {
                DataHandlerView.displayReadResults(handler.readFromCSVToEmployees(CSV_CHOICES.get(choice)));
                handler.employeesToWriteableEmployees(handler.getEmployeesArr());
            } else {
                DataHandlerView.displayReadResults(
                        handler.functionalReadFromCSVToWriteableEmployees(CSV_CHOICES.get(choice)));
            }
            String[] writeStats = JDBCDriver.insertAllBatchesOf100(
                    handler.getWriteableEmployeesArr(), DATABASE_CONNECTIONS.get(dbChoice));
            DataHandlerView.displayInsertResults(writeStats);
            logger.info("Writing stats: [#rows:" + writeStats[0] + ", timeTaken:" + writeStats[1] + "]");
        }
    }

//    public void readAndWrite() {
//        String choice = DataHandlerView.getInput(CSV_CHOICES, "a file to read from.");
//        System.out.println("------ Reading data from CSV ------");
//        String route = DataHandlerView.getInput(ROUTE_CHOICES, "a style route to take.");
//        String[] stats;
//        if (route.equals("o")) {
//            stats = handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
//        } else {
//            stats = handler.functionalReadFromCSVToWriteableEmployees(CSV_CHOICES.get(choice));
//        }
//        //
//        if (choice.equals("s")) {
//            DataHandlerView.displayReadResults(stats);
//            logger.info("Reading stats: [#rows:" + stats[0] + ", #rejects:" + stats[1] + ", #readTime:"
//                    + stats[2] + ", #createTime:" + stats[3] + "]");
//        } else {
//            writeEmployeesToDB(stats);
//        }
//    }
}

