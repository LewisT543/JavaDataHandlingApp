package com.sparta.data;

import com.sparta.data.models.Employee;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataHandler {
    private LinkedHashMap<Integer, String[]> rejects = new LinkedHashMap<>();
    // why am I using hashmaps... Rework into arrays.
    private LinkedHashMap<Integer, Employee> employeeObjectsMap = new LinkedHashMap<>();
    public void readAllToObjects(String filePath) {
        long start = System.nanoTime();
        List<String[]> objectDefinitions = CSVAccessor.readAllToList(filePath);
        long readStop = System.nanoTime();
        System.out.println("Read time taken: " + (readStop - start) + "ns");
        int i = 0;
        for (String[] list : objectDefinitions) {
            String[] datesCleaned = tryCleanDates(list);
            if (isValidEmployeeData(datesCleaned) && !isDuplicateEntry(datesCleaned))
                employeeObjectsMap.put(i, stringLstToEmployee(datesCleaned));
            else
                rejects.put(i, list);
            i++;
        }
        System.out.println(employeeObjectsMap.size());
        System.out.println(rejects);
    }

    public boolean isDuplicateEntry2(String[] dateCleanedData) {
        // This is awful
        StringBuilder newEmp = new StringBuilder();
        newEmp.append(dateCleanedData[1]).append(dateCleanedData[2]).append(dateCleanedData[3])
                .append(dateCleanedData[4]).append(dateCleanedData[7]);
        for (Map.Entry<Integer, Employee> entry : employeeObjectsMap.entrySet()) {
            Employee myEmp = entry.getValue();
            StringBuilder emp = new StringBuilder().append(myEmp.getNamePrefix())
                    .append(myEmp.getFirstName()).append(myEmp.getMiddleInitial())
                    .append(myEmp.getLastName()).append(myEmp.getDateOfBirth().toString());
            if (newEmp.toString().equals(emp.toString())) return true;
        }
        return false;
    }

    public boolean isDuplicateEntry(String[] dateCleanedData) {
        // This is awful
        StringBuilder newEmp = new StringBuilder();
        newEmp.append(dateCleanedData[0]).append(dateCleanedData[6]);
        for (Map.Entry<Integer, Employee> entry : employeeObjectsMap.entrySet()) {
            Employee myEmp = entry.getValue();
            StringBuilder emp = new StringBuilder().append(myEmp.getEmpID())
                    .append(myEmp.getEmail());
            if (newEmp.toString().equals(emp.toString())) return true;
        }
        return false;
    }

    public boolean isValidEmployeeData(String[] data) {
        boolean validData = true;
        try {
            int empId = Integer.parseInt(data[0]);
            if (empId < 0) validData = false;
            String namePrefix = data[1];
            if (namePrefix.length() > 5 || !namePrefix.matches("[a-zA-Z]+\\.")) validData = false;
            String firstName = data[2];
            if (firstName.length() > 256 || !firstName.matches("\\p{L}+")) validData = false;
            String middleInitial = data[3];
            if (middleInitial.length() > 3 || !middleInitial.matches("[a-zA-Z]+")) validData = false;
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

    public Employee stringLstToEmployee(String[] params) {
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

    public void printEmployees(LinkedHashMap<Integer, Employee> map) {
        for (Map.Entry<Integer, Employee> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public LinkedHashMap<Integer, String[]> getRejects() {
        return rejects;
    }
}
