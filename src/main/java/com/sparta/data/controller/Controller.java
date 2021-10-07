package com.sparta.data.controller;

import com.sparta.data.models.utils.DataHandler;
import com.sparta.data.models.utils.JDBCDriver;
import com.sparta.data.views.DataHandlerView;
import org.apache.log4j.Logger;

import java.util.Arrays;
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

    public Controller(DataHandler handler, Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public void readObjectsIn() {
        String choice = DataHandlerView.getInput(CSV_CHOICES, "a file to read from.");
        System.out.println("------ Reading data from CSV ------");
        String route = DataHandlerView.getInput()
        String[] stats = handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
        if (choice.equals("s")) {
            DataHandlerView.displayReadResults(stats);
            logger.info("Reading stats: [#rows:" + stats[0] + ", #rejects:" + stats[1] + ", #readTime:"
                    + stats[2] + ", #createTime:" + stats[3] + "]");
        } else {
            writeObjsToDB(stats);
        }
    }

    public void writeObjsToDB(String[] stats) {
        DataHandlerView.displayInitialisationResults(JDBCDriver.initialiseDb());
        DataHandlerView.displayReadResults(stats);
        logger.info("Reading stats: [#rows:" + stats[0] + ", #rejects:" + stats[1] + ", readTime:"
                + stats[2] + ", createTime:" + stats[3] + "]");

        System.out.println("------ Converting Employees to WriteableEmployees ------");
        handler.employeesToWriteableEmployees(handler.getEmployeesArr());

        String[] writeStats = JDBCDriver.insertAllBatchesOf100(handler.getWriteableEmployeesArr());
        DataHandlerView.displayInsertResults(writeStats);
        logger.info("Writing stats: [#rows:" + writeStats[0] + ", timeTaken:" + writeStats[1] + "]");
    }
}

