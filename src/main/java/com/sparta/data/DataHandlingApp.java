package com.sparta.data;

import com.sparta.data.controller.Controller;
import com.sparta.data.models.utils.DataHandler;
import com.sparta.data.models.utils.JDBCDriver;
import com.sparta.data.views.DataHandlerView;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class DataHandlingApp {
    private static final Logger myLogger = Logger.getLogger("OutputLogger");
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller(new DataHandler(), myLogger);
        PropertyConfigurator.configure("log4j.properties");
        String dbChoice = controller.createAndWrite();
        while (true) { controller.query(dbChoice); }
    }
}
