package com.sparta.data.tests;

import com.sparta.data.models.utils.DataHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;

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
}
