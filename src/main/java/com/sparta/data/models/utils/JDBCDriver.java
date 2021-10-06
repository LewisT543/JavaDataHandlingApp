package com.sparta.data.models.utils;

import com.sparta.data.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JDBCDriver {
    private static final HashMap<String, String> SQL_QUERIES = new HashMap<>() {{
        put("sqlDropEmp", "DROP TABLE IF EXISTS employees");
        put("sqlDropPref", "DROP TABLE IF EXISTS prefixs");
        put("sqlDropGen", "DROP TABLE IF EXISTS genders");
        put("sqlCreatePrefixTable", "CREATE TABLE prefixs (prefix_id INTEGER PRIMARY KEY," +
                "prefix VARCHAR(6) NOT NULL);");
        put("sqlPopulatePrefixTable", "INSERT INTO prefixs (prefix)" +
                "VALUES ('Mrs.'),( 'Mr.'), ('Ms.'), ('Dr.'), ('Drs.'), ('Hon.'), ('Prof.')");
        put("sqlCreateGenderTable", "CREATE TABLE genders (" +
                "gender_id INTEGER PRIMARY KEY,gender VARCHAR(1) NOT NULL);");
        put("sqlPopulateGenderTable", "INSERT INTO genders (gender)" +
                "VALUES ('M'), ('F')");
        put("sqlCreateEmpTable", "CREATE TABLE employees (id INTEGER PRIMARY KEY NOT NULL, employeeID INTEGER, " +
                "prefix_id INTEGER NOT NULL, f_name VARCHAR(255) NOT NULL, mid_initial VARCHAR(5)," +
                "l_name VARCHAR(255) NOT NULL, gender_id INTEGER NOT NULL, email VARCHAR(255) NOT NULL," +
                "date_of_birth DATE NOT NULL, date_of_joining DATE NOT NULL, salary INTEGER, " +
                "FOREIGN KEY (prefix_id) REFERENCES prefixs (prefix_id), " +
                "FOREIGN KEY (gender_id) REFERENCES genders (gender_id));");

        put("sqlInsert", "INSERT INTO employees (employeeID, prefix_id, f_name, mid_initial, l_name, gender_id, " +
                "email, date_of_birth, date_of_joining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
    }};
    public static long initialiseDb() {
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:employees.db")) {
            Statement statement = conn.createStatement();
            statement.execute(SQL_QUERIES.get("sqlDropEmp"));
            statement.execute(SQL_QUERIES.get("sqlDropPref"));
            statement.execute(SQL_QUERIES.get("sqlDropGen"));

            statement.execute(SQL_QUERIES.get("sqlCreatePrefixTable"));
            statement.execute(SQL_QUERIES.get("sqlPopulatePrefixTable"));
            statement.execute(SQL_QUERIES.get("sqlCreateGenderTable"));
            statement.execute(SQL_QUERIES.get("sqlPopulateGenderTable"));
            statement.execute(SQL_QUERIES.get("sqlCreateEmpTable"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        long stop = System.nanoTime();
        return (stop - start) / 1000000;
    }

    // Add data
    public static String[] insertAll(ArrayList<Employee> employees) {
        int totalRows = 0;
        PreparedStatement statement = null;
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:employees.db")) {
            for (Employee emp : employees) {
                statement = conn.prepareStatement(SQL_QUERIES.get("sqlInsert"));
                statement.setInt(1, emp.getEmpID());
                switch (emp.getNamePrefix()) {
                    case "Mr."   -> statement.setInt(2, 1);
                    case "Mrs."  -> statement.setInt(2, 2);
                    case "Ms."   -> statement.setInt(2,3);
                    case "Dr."   -> statement.setInt(2, 4);
                    case "Drs."  -> statement.setInt(2, 5);
                    case "Hon."  -> statement.setInt(2, 6);
                    case "Prof." -> statement.setInt(2, 7);
                }
                statement.setString(3, emp.getFirstName());
                statement.setString(4, emp.getMiddleInitial());
                statement.setString(5, emp.getLastName());

                if (emp.getGender().equals("M")) statement.setInt(6, 1);
                else statement.setInt(6, 2);

                statement.setString(7, emp.getEmail());
                statement.setDate(8, emp.getDateOfBirth());
                statement.setDate(9, emp.getDateOfJoining());
                statement.setInt(10, emp.getSalary());
                int rowsInserted = statement.executeUpdate();
                totalRows += rowsInserted;
                close(statement);
                System.out.println("Employee added.");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            close(statement);
        }
        long stop = System.nanoTime();
        String[] results = new String[2];
        results[0] = String.valueOf(totalRows);
        results[1] = String.valueOf((stop - start) / 1000000);
        return results;
    }
    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}