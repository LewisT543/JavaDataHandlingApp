package com.sparta.data.models.utils;

import com.sparta.data.models.Employee;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataHandler {
    private ArrayList<Employee> employeesArr = new ArrayList<>();
    public String[] readFromCSVToEmployees(String filePath) {
        long start = System.nanoTime();
        List<String[]> listOfStrArr = CSVAccessor.readCSVToList(filePath);
        long readStop = System.nanoTime();
        int i = 0;
        long createObjsStart = System.nanoTime();
        for (String[] data : listOfStrArr) {
            data = tryCleanDates(data);
            if (isValidEmployeeData(data))
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

    public boolean isValidEmployeeData(String[] data) {
        boolean validData = true;
        try {
            int empId = Integer.parseInt(data[0]);
            if (empId < 0) validData = false;
            String namePrefix = data[1];
            if (namePrefix.length() > 5 || !namePrefix.matches("[a-zA-Z]+\\.")) validData = false;
            String firstName = data[2];
            if (firstName.length() > 255 || !firstName.matches("\\p{L}+")) validData = false;
            String middleInitial = data[3];
            if (middleInitial.length() > 5 || !middleInitial.matches("[a-zA-Z\\.]+")) validData = false;
            String lastName = data[4];
            if (lastName.length() > 256 || !lastName.matches("\\p{L}+")) validData = false;
            String gender = data[5];
            if (!gender.matches("[FMfm]")) validData = false;
            String email = data[6];
            if (!email.matches("^(.+)@(.+)$")) validData = false;
            Date dateOfBirth = Date.valueOf(data[7]);
            Date dateOfJoining = Date.valueOf(data[8]);
            int salary  = Integer.parseInt(data[9]);
            if (salary < 0) validData = false;
        } catch (IllegalArgumentException e) {
            validData = false;
            System.err.println("Invalid input");
        }
        if (!validData) System.out.println("Invalid Data.");
        return validData;
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

    public ArrayList<Employee> getEmployeesArr() {
        return employeesArr;
    }
}
