package com.sparta.data.models.utils;

import com.sparta.data.models.Employee;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataHandler {
    private ArrayList<Employee> employeesArr = new ArrayList<>();
    public void readFromCSVToEmployees(String filePath) {
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
        System.out.println("Size of valid object Map: " + employeesArr.size());
        System.out.println("Size of rejects: " + CSVAccessor.getDuplicates().size());
        System.out.println("Total time taken to read: " + ((readStop - start) / 1000000) + "ms");
        System.out.println("Total time taken to create objects: " + ((createStop - createObjsStart) / 1000000) + "ms");
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
            if (middleInitial.length() > 5 || !middleInitial.matches("[a-zA-Z]\\.+")) validData = false;
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
        int empId = Integer.parseInt(params[0]);
        String namePrefix = params[1];
        String firstName = params[2];
        String middleInitial = params[3];
        String lastName = params[4];
        String gender = params[5];
        String email = params[6];
        Date dateOfBirth = Date.valueOf(params[7]);
        Date dateOfJoining = Date.valueOf(params[8]);
        int salary  = Integer.parseInt(params[9]);
        return new Employee(empId, namePrefix, firstName, middleInitial, lastName, gender,
                email, dateOfBirth, dateOfJoining, salary);
    }
}
