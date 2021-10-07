package com.sparta.data.models.utils;

import com.sparta.data.models.Employee;
import com.sparta.data.models.WriteableEmployee;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataHandler {
    private ArrayList<Employee> employeesArr = new ArrayList<>();
    private ArrayList<WriteableEmployee> writeableEmployeesArr = new ArrayList<>();
    public String[] readFromCSVToEmployees(String filePath) {
        long start = System.nanoTime();
        List<String[]> listOfStrArr = CSVAccessor.readCSVToList(filePath);
        long readStop = System.nanoTime();
        int i = 0;
        long createObjsStart = System.nanoTime();
        for (String[] data : listOfStrArr) {
            data = tryCleanDates(data);
            if (DataValidator.isValidEmployeeData(data))
                employeesArr.add(stringArrToEmployee(data));
            i++;
        }
        long createStop = System.nanoTime();
        String[] returnArr = new String[4];
        returnArr[0] = String.valueOf(employeesArr.size());
        returnArr[1] = String.valueOf(CSVAccessor.getDuplicates().size());
        returnArr[2] = String.valueOf((readStop - start) / 1000000);
        returnArr[3] = String.valueOf((createStop - createObjsStart) / 1000000);
        return returnArr;
    }

    public String[] tryCleanDates(String[] data) {
        String dateOfBirth = data[7];
        String dateOfJoin = data[8];
        try {
            java.util.Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(dateOfBirth);
            java.sql.Date sqlDob = new java.sql.Date(dob.getTime());
            java.util.Date doj = new SimpleDateFormat("MM/dd/yyyy").parse(dateOfJoin);
            java.sql.Date sqlDoj = new java.sql.Date(doj.getTime());
            data[7] = sqlDob.toString();
            data[8] = sqlDoj.toString();
        } catch (ParseException e) {
            System.err.println(e);
        }
        return data;
    }

    public Employee stringArrToEmployee(String[] params) {
        return new Employee(Integer.parseInt(params[0]), params[1], params[2], params[3], params[4], params[5],
                params[6], Date.valueOf(params[7]), Date.valueOf(params[8]), Integer.parseInt(params[9]));
    }

    public void employeesToWriteableEmployees(ArrayList<Employee> employees) {
        int i = 1;
        for (Employee employee : employees) {
            writeableEmployeesArr.add(new WriteableEmployee(employee, i));
            i ++;
        }
        System.out.println("Employees converted.");
    }

    public ArrayList<Employee> getEmployeesArr() {
        return employeesArr;
    }

    public ArrayList<WriteableEmployee> getWriteableEmployeesArr() {
        return writeableEmployeesArr;
    }
}
