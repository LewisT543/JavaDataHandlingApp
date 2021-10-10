package com.sparta.data.tests;

import com.sparta.data.models.Employee;
import com.sparta.data.models.WriteableEmployee;
import com.sparta.data.models.utils.DataHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataHandlerTest {
    @Test
    public void readFromCSVToEmployeesResultsTest() {
        DataHandler handler = new DataHandler();
        String[] result = handler.readFromCSVToEmployees("EmployeeRecordsTest.csv");
        System.out.println(Arrays.toString(result));
        assertEquals(String.valueOf(5), result[0]);
    }
    @Test
    public void readFromCSVToEmployeesValidEmployeeArrayTest() {
        DataHandler handler = new DataHandler();
        String[] result = handler.readFromCSVToEmployees("EmployeeRecordsTest.csv");
        ArrayList<Employee> employees = handler.getEmployeesArr();
        assertAll(
                () -> assertEquals(5 ,employees.size()),
                () -> assertNotNull(employees.get(0)),
                () -> assertNotNull(employees.get(1)),
                () -> assertNotNull(employees.get(2)),
                () -> assertNotNull(employees.get(3)),
                () -> assertNotNull(employees.get(4))
        );
    }

    @Test
    public void functionalReadFromCSVToWriteableEmployeesResultsTest() {
        DataHandler handler = new DataHandler();
        String[] result = handler.functionalReadFromCSVToWriteableEmployees("EmployeeRecordsTest.csv");
        System.out.println(Arrays.toString(result));
        assertEquals(String.valueOf(5), result[0]);
    }

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
    public void stringArrToEmployeeTest() {
        String[] validData = {"1","Mrs.","Lavon","A","Shufelt","F","lavon.shufelt@aol.com","1977-12-19","2000-07-23","184597"};
        Employee myEmp = new Employee(Integer.parseInt(validData[0]), validData[1], validData[2], validData[3], validData[4], validData[5],
                validData[6], Date.valueOf(validData[7]), Date.valueOf(validData[8]), Integer.parseInt(validData[9]));
        assertNotNull(myEmp);
    }

    @Test
    public void employeesToWriteableEmployeesTest() {
        String[] validData = {"1","Mrs.","Lavon","A","Shufelt","F","lavon.shufelt@aol.com","1977-12-19","2000-07-23","184597"};
        String[] validData2 = {"2","Mr.","Titus","X","Shrewsbury","M","titus.shrewsbury@hotmail.com","1993-06-10","2017-06-14","190460"};
        Employee myEmp1 = new Employee(Integer.parseInt(validData[0]), validData[1], validData[2], validData[3], validData[4], validData[5],
                validData[6], Date.valueOf(validData[7]), Date.valueOf(validData[8]), Integer.parseInt(validData[9]));
        Employee myEmp2 = new Employee(Integer.parseInt(validData2[0]), validData2[1], validData2[2], validData2[3], validData2[4], validData2[5],
                validData2[6], Date.valueOf(validData2[7]), Date.valueOf(validData2[8]), Integer.parseInt(validData2[9]));
        DataHandler handler = new DataHandler();
        ArrayList<Employee> myEmps = new ArrayList<>() {{
            add(myEmp1);
            add(myEmp2);
        }};
        handler.employeesToWriteableEmployees(myEmps);
        assertAll(
                () -> assertEquals(2, handler.getWriteableEmployeesArr().size()),

                () -> assertEquals(2, handler.getWriteableEmployeesArr().get(0).getPrefixId()),
                () -> assertEquals(1, handler.getWriteableEmployeesArr().get(1).getPrefixId()),
                () -> assertEquals(2, handler.getWriteableEmployeesArr().get(0).getGenderId()),
                () -> assertEquals(1, handler.getWriteableEmployeesArr().get(1).getGenderId())
        );
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
                () -> Assertions.assertEquals(16375, myLists.get(2).size()),
                () -> Assertions.assertEquals(16375, myLists.get(3).size())
        );
    }

    @Test
    public void getEmployeesArrTest() {
        DataHandler handler = new DataHandler();
        handler.readFromCSVToEmployees("EmployeeRecordsTest.csv");
        assertNotNull(handler.getEmployeesArr());
    }

    @Test
    public void getWriteableEmployeesArrTest() {
        DataHandler handler = new DataHandler();
        handler.functionalReadFromCSVToWriteableEmployees("EmployeeRecordsTest.csv");
        assertNotNull(handler.getWriteableEmployeesArr());
    }
}
