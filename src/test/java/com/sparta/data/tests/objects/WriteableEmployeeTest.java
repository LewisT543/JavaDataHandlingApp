package com.sparta.data.tests.objects;

import com.sparta.data.models.Employee;
import com.sparta.data.models.WriteableEmployee;
import com.sparta.data.models.utils.DataHandler;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WriteableEmployeeTest {
    private String[] validData = {"1","Mrs.","Lavon","A","Shufelt","F","lavon.shufelt@aol.com","1977-12-19","2000-07-23","184597"};
    @Test
    public void createWriteableEmployeeTest() {
        Employee emp = new Employee(Integer.parseInt(validData[0]), validData[1], validData[2], validData[3], validData[4], validData[5],
                validData[6], Date.valueOf(validData[7]), Date.valueOf(validData[8]), Integer.parseInt(validData[9]));
        ArrayList<Employee> empArr = new ArrayList<>() {{ add(emp); }};
        DataHandler handler = new DataHandler();
        handler.employeesToWriteableEmployees(empArr);
        WriteableEmployee writeableEmp = handler.getWriteableEmployeesArr().get(0);
        assertAll(
                () -> assertEquals(1, writeableEmp.getEmpNumber()),
                () -> assertEquals(2, writeableEmp.getPrefixId()),
                () -> assertEquals("Lavon", writeableEmp.getFirstName()),
                () -> assertEquals("A", writeableEmp.getMiddleInitial()),
                () -> assertEquals("Shufelt", writeableEmp.getLastName()),
                () -> assertEquals(2, writeableEmp.getGenderId()),
                () -> assertEquals("lavon.shufelt@aol.com", writeableEmp.getEmail()),
                () -> assertEquals(Date.valueOf("1977-12-19"), writeableEmp.getDateOfBirth()),
                () -> assertEquals(Date.valueOf("2000-07-23"), writeableEmp.getDateOfJoining()),
                () -> assertEquals(184597, writeableEmp.getSalary())
        );
    }
}
