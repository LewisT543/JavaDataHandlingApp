package com.sparta.data.controller;

import com.sparta.data.models.utils.DataHandler;
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
        handler.readFromCSVToEmployees(CSV_CHOICES.get(choice));
        // Write to CSV or DB code goes here.
    }
}

