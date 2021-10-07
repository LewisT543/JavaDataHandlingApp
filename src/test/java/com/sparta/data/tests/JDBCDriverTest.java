package com.sparta.data.tests;

import com.sparta.data.models.Employee;
import com.sparta.data.models.utils.JDBCDriver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JDBCDriverTest {
    @Test
    public void initialiseDbTest() {
        System.out.println("Time taken to initialise DB: " + JDBCDriver.initialiseDb());
        File file = new File("employees.db");
        assertTrue(file.exists());
        // TODO: read the file. Check that it contains what it's supposed to contain
    }

    @Test
    public void insertTest() {
        // "yyyy-[m]m-[d]d"
//        ArrayList<Employee> employees = new ArrayList<>();
//        employees.add(new Employee(945178,"Ms.","Beulah","J","Weeks", "F",
//                "beulah.weeks@aol.com", Date.valueOf("1975-4-5"), Date.valueOf("2011-2-11"),139978));
//        employees.add(new Employee(942222,"Ms.","Deulah","J","Reeks", "M",
//                "beulah.bop@aol.com", Date.valueOf("1975-4-10"), Date.valueOf("2011-6-11"),159978));
//        JDBCDriver.insertAllBatchesOf100(employees);
        // TODO: check the db for inserts.
    }
}
