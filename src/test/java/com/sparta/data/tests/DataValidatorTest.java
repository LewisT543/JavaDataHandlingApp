package com.sparta.data.tests;

import com.sparta.data.models.utils.DataHandler;
import com.sparta.data.models.utils.DataValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Loads of tests to write for the regex stuff

public class DataValidatorTest {
    @Test
    public void isValidDataDatesTest() {
        String[] myData = {"1","Mrs.","Lavon","A","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        DataValidator.isValidEmployeeData(cleanData);
        assertAll(
                () -> assertEquals("1977-12-19" , myData[7]),
                () -> assertEquals("2000-07-23", myData[8])
        );
    }

    @Test
    public void isValidDataValidTest() {
        String[] myData = {"1","Mrs.","Lavon","A","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertTrue(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidEmpNumberTest() {
        String[] myData = {"g","Mrs.","Lavon","A","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidPrefixTest() {
        String[] myData = {"1","123.","Lavon","A","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidInitialTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidGenderTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","X",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidEmailTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","F",
                "lavon.shufeltaol.com","12/19/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidDOBTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","F",
                "lavon.shufelt@aol.com","19/12/1977","7/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidDOJTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","JUL/23/2000","184597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }

    @Test
    public void isValidDataInvalidSalaryTest() {
        String[] myData = {"1","Mrs.","Lavon","","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184X597"};
        DataHandler myHandler = new DataHandler();
        String[] cleanData = myHandler.tryCleanDates(myData);
        assertFalse(DataValidator.isValidEmployeeData(cleanData));
    }
}
