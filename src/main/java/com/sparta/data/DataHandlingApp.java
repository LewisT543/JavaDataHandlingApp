package com.sparta.data;

import com.sparta.data.controller.Controller;
import com.sparta.data.models.utils.CSVAccessor;
import com.sparta.data.models.utils.DataHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

// Callable statement seems to be the fastest writing method, provided I can create procedures in my DB.

public class DataHandlingApp {
    private static final Logger errorLogger = Logger.getLogger("ErrorLogger");
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller(new DataHandler(), errorLogger);
        PropertyConfigurator.configure("log4j.properties");

//        DataHandlerView.getInput()
        parseEmployeeObjects("EmployeeRecords.csv", "rejects.csv");
    }

    public static void parseEmployeeObjects (String inPath, String outPath) {
        DataHandler handler = new DataHandler();
        handler.readFromCSVToEmployees(inPath);
        CSVAccessor.writeArrLstStrToCSV(outPath, CSVAccessor.getDuplicates());
    }
}
