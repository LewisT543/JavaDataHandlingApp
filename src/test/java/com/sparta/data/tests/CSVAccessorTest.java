package com.sparta.data.tests;

import com.sparta.data.models.utils.CSVAccessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVAccessorTest {
    @Test
    public void readCSVToListTest() {
        List<String[]> myLines = CSVAccessor.readCSVToList("EmployeeRecordsTest.csv");
        assertAll(
                () -> assertEquals(5, myLines.size()),
                () -> assertNotNull(myLines.get(0)),
                () -> assertNotNull(myLines.get(1)),
                () -> assertNotNull(myLines.get(2)),
                () -> assertNotNull(myLines.get(3)),
                () -> assertNotNull(myLines.get(4))
        );
    }

    @Test
    public void readCSVToListWith1DupeTest() {
        List<String[]> myLines = CSVAccessor.readCSVToList("EmployeeRecordsTest2.csv");
        assertAll(
                () -> assertEquals(5, myLines.size()),
                () -> assertNotNull(myLines.get(0)),
                () -> assertNotNull(myLines.get(1)),
                () -> assertNotNull(myLines.get(2)),
                () -> assertNotNull(myLines.get(3)),
                () -> assertNotNull(myLines.get(4)),
                () -> assertEquals(1, CSVAccessor.getDuplicates().size())
        );
    }
}
