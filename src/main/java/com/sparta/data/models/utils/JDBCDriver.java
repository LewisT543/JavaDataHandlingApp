package com.sparta.data.models.utils;

import com.sparta.data.models.WriteableEmployee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

// Runtime.getRuntime().availableProcessors()
// TODO: Something wrong with dates, explore different types compatible with SQLite
// TODO: for some reason my employeeID is auto incrementing, nothing in my SQL says it should, but it is anyway

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
        put("sqlCreateEmpTable", "CREATE TABLE employees (id INTEGER PRIMARY KEY,  " +
                "prefix_id INTEGER NOT NULL, f_name VARCHAR(255) NOT NULL, mid_initial VARCHAR(5)," +
                "l_name VARCHAR(255) NOT NULL, gender_id INTEGER NOT NULL, email VARCHAR(255) NOT NULL," +
                "date_of_birth DATE, date_of_joining DATE, salary INTEGER, " +
                "FOREIGN KEY (prefix_id) REFERENCES prefixs (prefix_id), " +
                "FOREIGN KEY (gender_id) REFERENCES genders (gender_id));");

        put("sqlInsert", "INSERT INTO employees (prefix_id, f_name, mid_initial, l_name, gender_id, " +
                "email, date_of_birth, date_of_joining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
    }};
    public static long initialiseDb(String dbConnection) {
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(dbConnection)) {
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
    public static String[] insertAllBatchesOf100(ArrayList<WriteableEmployee> employees, String dbConnection) {
        int totalRows = 0;
        PreparedStatement statement = null;
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(dbConnection)) {
            int j = 0;
            int iterations = 0;
            for (WriteableEmployee emp : employees) {
                statement = conn.prepareStatement(SQL_QUERIES.get("sqlInsert"));
                statement.setInt(1, emp.getPrefixId());
                statement.setString(2, emp.getFirstName());
                statement.setString(3, emp.getMiddleInitial());
                statement.setString(4, emp.getLastName());
                statement.setInt(5, emp.getGenderId());
                statement.setString(6, emp.getEmail());
                statement.setDate(7, emp.getDateOfBirth());
                statement.setDate(8, emp.getDateOfJoining());
                statement.setInt(9, emp.getSalary());
                statement.addBatch();
                j++;
                iterations++;
                if (j >= 100 || employees.size() - iterations < 100) {
                    conn.setAutoCommit(false);
                    statement.executeBatch();
                    conn.setAutoCommit(true);
                    totalRows += j;
                    j = 0;
                }
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