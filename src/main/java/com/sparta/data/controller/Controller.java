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

    public Controller(DataHandler handler, Logger logger) {
        this.handler = handler;
        this.logger = logger;
    }

    public void readObjectsIn() {
        String choice = DataHandlerView.getInput(CSV_CHOICES, "a file to read from.");
        String[] stats = handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
        System.out.println("------ Reading data from CSV ------");
        if (choice.equals("s")) {
            DataHandlerView.displayReadResults(stats);
        } else {
            System.out.println("------ Writing data to database - this could take some time... ------");
            writeObjsToDB(stats);
        }
    }

    public void writeObjsToDB(String[] stats) {
        DataHandlerView.displayInitialisationResults(JDBCDriver.initialiseDb());
        DataHandlerView.displayReadResults(stats);
        DataHandlerView.displayInsertResults(JDBCDriver.insertAll(handler.getEmployeesArr()));
    }
}

