package com.sparta.data.tests.objects;

import com.sparta.data.models.Employee;
import org.junit.jupiter.api.Test;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {
    private String[] validData = {"1","Mrs.","Lavon","A","Shufelt","F","lavon.shufelt@aol.com","1977-12-19","2000-07-23","184597"};
    @Test
    public void createEmployeeTest() {
        Employee myEmp = new Employee(Integer.parseInt(validData[0]), validData[1], validData[2], validData[3], validData[4], validData[5],
                validData[6], Date.valueOf(validData[7]), Date.valueOf(validData[8]), Integer.parseInt(validData[9]));
        assertAll(
                () -> assertEquals(1, myEmp.getEmpNumber()),
                () -> assertEquals("Mrs.", myEmp.getNamePrefix()),
                () -> assertEquals("Lavon", myEmp.getFirstName()),
                () -> assertEquals("A", myEmp.getMiddleInitial()),
                () -> assertEquals("Shufelt", myEmp.getLastName()),
                () -> assertEquals("F", myEmp.getGender()),
                () -> assertEquals("lavon.shufelt@aol.com", myEmp.getEmail()),
                () -> assertEquals(Date.valueOf("1977-12-19"), myEmp.getDateOfBirth()),
                () -> assertEquals(Date.valueOf("2000-07-23"), myEmp.getDateOfJoining()),
                () -> assertEquals(184597, myEmp.getSalary())
        );
    }
}
