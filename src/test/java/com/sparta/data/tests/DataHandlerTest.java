package com.sparta.data.tests;

import com.sparta.data.models.WriteableEmployee;
import com.sparta.data.models.utils.DataHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

public class DataHandlerTest {
    @Test
    public void tryCleanDatesTest() {
        String[] data = {null, null, null, null, null, null, null, "9/21/1982", "2/1/2008", null};
        DataHandler handler = new DataHandler();
        String[] validData = handler.tryCleanDates(data);
        Assertions.assertEquals("1982-09-21", validData[7]);
        System.out.println(Date.valueOf("1982-09-21"));
        Assertions.assertEquals("2008-02-01", validData[8]);
        System.out.println(Date.valueOf("2008-02-01"));
    }

    @Test
    public void splitEmployeeListLengthsTest() {
        DataHandler myHandler = new DataHandler();
        myHandler.functionalReadFromCSVToWriteableEmployees("EmployeeRecordsLarge.csv");
        ArrayList<List<WriteableEmployee>> myLists = DataHandler.splitEmployeeList(myHandler.getWriteableEmployeesArr(), 4);
        assertAll(
                () -> Assertions.assertEquals(4, myLists.size()),
                () -> Assertions.assertEquals(16374, myLists.get(0).size()),
                () -> Assertions.assertEquals(16375, myLists.get(1).size()),
                () -> Assertions.assertEquals(16374, myLists.get(2).size()),
                () -> Assertions.assertEquals(16374, myLists.get(3).size())
        );
    }
}
