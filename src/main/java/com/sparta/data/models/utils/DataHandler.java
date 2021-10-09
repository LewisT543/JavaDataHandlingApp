package com.sparta.data.models.utils;

import com.sparta.data.models.Employee;
import com.sparta.data.models.WriteableEmployee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataHandler {
    private ArrayList<Employee> employeesArr = new ArrayList<>();
    private ArrayList<WriteableEmployee> writeableEmployeesArr = new ArrayList<>();
    public String[] readFromCSVToEmployees(String filePath) {
        long start = System.nanoTime();
        List<String[]> listOfStrArr = CSVAccessor.readCSVToList(filePath);
        long readStop = System.nanoTime();
        long createObjsStart = System.nanoTime();
        for (String[] data : listOfStrArr) {
            data = tryCleanDates(data);
            if (DataValidator.isValidEmployeeData(data))
                employeesArr.add(stringArrToEmployee(data));
        }
        long createObjsStop = System.nanoTime();
        String[] returnArr = new String[4];
        returnArr[0] = String.valueOf(employeesArr.size());
        returnArr[1] = String.valueOf(CSVAccessor.getDuplicates().size());
        returnArr[2] = String.valueOf((readStop - start) / 1000000);
        returnArr[3] = String.valueOf((createObjsStop - createObjsStart) / 1000000);
        return returnArr;
    }

    public String[] functionalReadFromCSVToWriteableEmployees(String filePath) {
        // This is absurdly powerful. 90% of my program in 1 stream. Cool, but doesn't save duplicates.
        // According to the internet, this is stupid, and I should just use the apache commons CSV library...
        long start = System.nanoTime();
        try {
            ArrayList<String[]> cleanData = (Files.lines(Paths.get(filePath))
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(this::tryCleanDates)
                    .filter(DataValidator::isValidEmployeeData)
                    .collect(Collectors.toCollection(ArrayList::new)));
            for (String[] line : cleanData) {
                employeesArr.add(stringArrToEmployee(line));
            }
            employeesToWriteableEmployees(employeesArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long createObjsStop = System.nanoTime();
        String[] returnArr = new String[4];
        returnArr[0] = String.valueOf(employeesArr.size());
        returnArr[1] = String.valueOf(CSVAccessor.getDuplicates().size());
        returnArr[2] = String.valueOf((createObjsStop - start) / 1000000);
        returnArr[3] = "All steps done in one, see read time taken for total";
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
        writeableEmployeesArr = employees
                .stream()
                .map(WriteableEmployee::new)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("------ Employees converted ------");
    }

    public static ArrayList<List<WriteableEmployee>> splitEmployeeList(ArrayList<WriteableEmployee> employees, int numThreads) {
        int chunkSize = employees.size() / numThreads;
        int leftOvers = employees.size() % numThreads;
        AtomicInteger counter = new AtomicInteger();
        Collection<List<WriteableEmployee>> partitionedList = employees
                .stream()
                .collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                .values();
        ArrayList<List<WriteableEmployee>> partitionedArrays = new ArrayList<>(partitionedList);
        for (int i = leftOvers; i > 0; i--) {
            partitionedArrays.get(i).add(employees.get(employees.size() - i));
        }
        return partitionedArrays;
    }

    public ArrayList<Employee> getEmployeesArr() {
        return employeesArr;
    }

    public ArrayList<WriteableEmployee> getWriteableEmployeesArr() {
        return writeableEmployeesArr;
    }
}
