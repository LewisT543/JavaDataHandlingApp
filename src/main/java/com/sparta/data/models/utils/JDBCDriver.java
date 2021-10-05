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

        put("sqlInsert", "INSERT INTO employees (employeeID, prefix_id, f_name, mid_initial, l_name, gender_id, email, " +
                "date_of_birth, date_of_joining, salary)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }};
    public static void initialiseDb() {
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
    }

    // Add data
    public static void insert(ArrayList<Employee> employees) {
        int totalRows = 0;
        PreparedStatement statement = null;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:employees.db")) {
            for (Employee emp : employees) {
                statement = conn.prepareStatement(SQL_QUERIES.get("sqlInsert"));
                statement.setInt(2, emp.getEmpID());
                switch (emp.getNamePrefix()) {
                    case "Mr."   -> statement.setInt(3, 1);
                    case "Mrs."  -> statement.setInt(3, 2);
                    case "Ms."   -> statement.setInt(3,3);
                    case "Dr."   -> statement.setInt(3, 4);
                    case "Drs."  -> statement.setInt(3, 5);
                    case "Hon."  -> statement.setInt(3, 6);
                    case "Prof." -> statement.setInt(3, 7);
                }
                statement.setString(3, emp.getNamePrefix());
                statement.setString(4, emp.getFirstName());
                statement.setString(5, emp.getMiddleInitial());
                statement.setString(6, emp.getLastName());

                if (emp.getGender().equals("M")) statement.setInt(7, 1);
                else statement.setInt(7, 2);

                statement.setString(8, emp.getEmail());
                statement.setDate(9, emp.getDateOfBirth());
                statement.setDate(10, emp.getDateOfJoining());
                int rowsInserted = statement.executeUpdate();
                totalRows += rowsInserted;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            close(statement);
        }
        System.out.println("Number of rows written: " + totalRows);
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