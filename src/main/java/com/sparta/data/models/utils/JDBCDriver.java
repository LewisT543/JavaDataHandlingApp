package com.sparta.data.models.utils;

import com.sparta.data.models.WriteableEmployee;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// Cannot find the "my.ini" configuration file anywhere to make these changes default...
// BEFORE RUNNING: Configure MySQL server modes
// SET GLOBAL sql_mode = 'modes';
// SET GLOBAL innodb_strict_mode=OFF;
// Check it has worked with:
// SELECT @@GLOBAL.sql_mode;

public class JDBCDriver {
    private static final HashMap<String, String> SQL_QUERIES = new HashMap<>() {{
        put("sqlDropEmp", "DROP TABLE IF EXISTS employees");
        put("sqlDropPref", "DROP TABLE IF EXISTS prefixs");
        put("sqlDropGen", "DROP TABLE IF EXISTS genders");
        put("sqlCreatePrefixTable", "CREATE TABLE prefixs (prefix_id INTEGER PRIMARY KEY NOT NULL /*!40101 AUTO_INCREMENT */," +
                "prefix VARCHAR(6));");
        put("sqlPopulatePrefixTable", "INSERT INTO prefixs (prefix)" +
                "VALUES ('Mrs.'),( 'Mr.'), ('Ms.'), ('Dr.'), ('Drs.'), ('Hon.'), ('Prof.')");
        put("sqlCreateGenderTable", "CREATE TABLE genders (" +
                "gender_id INTEGER PRIMARY KEY NOT NULL /*!40101 AUTO_INCREMENT */,gender VARCHAR(1) NOT NULL);");
        put("sqlPopulateGenderTable", "INSERT INTO genders (gender)" +
                "VALUES ('M'), ('F')");
        put("sqlCreateEmpTable", "CREATE TABLE employees (id INTEGER PRIMARY KEY NOT NULL /*!40101 AUTO_INCREMENT */, " +
                "employee_number INTEGER, prefix_id INTEGER, f_name VARCHAR(255), mid_initial VARCHAR(5)," +
                "l_name VARCHAR(255), gender_id INTEGER, email VARCHAR(255)," +
                "date_of_birth DATE, date_of_joining DATE, salary INTEGER, " +
                "FOREIGN KEY (prefix_id) REFERENCES prefixs (prefix_id), " +
                "FOREIGN KEY (gender_id) REFERENCES genders (gender_id));");

        put("sqlInsert", "INSERT INTO employees (employee_number, prefix_id, f_name, mid_initial, l_name, gender_id, " +
                "email, date_of_birth, date_of_joining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }};
    public static long initialiseDb(String dbConnection) {
        String user = null;
        String pass = null;
        if (dbConnection.equals("jdbc:mysql://localhost:3306/employees?rewriteBatchedStatements=true")){
            user = "root";
            pass = "123xyz";
        }
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(dbConnection, user, pass)) {
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
        String user = null;
        String pass = null;
        if (dbConnection.equals("jdbc:mysql://localhost:3306/employees?rewriteBatchedStatements=true")){
            user = "root";
            pass = "123xyz";// If security is a concern, use an environment variable or a properties file to store pass.
        }
        int totalRows = 0;
        PreparedStatement statement = null;
        long start = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(dbConnection, user, pass)) {
            conn.setAutoCommit(false);
            int count = 0;
            int BATCH_SIZE = 1000;
            statement = conn.prepareStatement(SQL_QUERIES.get("sqlInsert"));
            for (WriteableEmployee emp : employees) {
                statement.setInt(1, emp.getEmpNumber());
                statement.setInt(2, emp.getPrefixId());
                statement.setString(3, emp.getFirstName());
                statement.setString(4, emp.getMiddleInitial());
                statement.setString(5, emp.getLastName());
                statement.setInt(6, emp.getGenderId());
                statement.setString(7, emp.getEmail());
                statement.setDate(8, emp.getDateOfBirth());
                statement.setDate(9, emp.getDateOfJoining());
                statement.setInt(10, emp.getSalary());
                statement.addBatch();
                if (++count % BATCH_SIZE == 0) {
                    int[] result = statement.executeBatch();
                    int rows = ((Arrays.stream(result).sum()) / 2) * -1;
                    totalRows += rows;
                    conn.commit();
                }
            }
            int[] result = statement.executeBatch();
            int rows = ((Arrays.stream(result).sum()) / 2) * -1;
            totalRows += rows;
            conn.commit();
            close(statement);
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

    public static String[] threadedInsert(ArrayList<WriteableEmployee> employees, String dbConnection, int numThreads) {
        ArrayList<List<WriteableEmployee>> splitEmployees = DataHandler.splitEmployeeList(employees, numThreads);
        ArrayList<ThreadDriver> threads = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 1; i <= numThreads; i++) {
            threads.add(new ThreadDriver(i, splitEmployees.get(i-1)));
        }
        for (ThreadDriver thread : threads) {
            thread.start();
        }
        System.out.println("------ Asynchronous writing to database. ------");
        try {
            for (ThreadDriver thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------ Threads completed. ------");
        long stop = System.nanoTime();
        String[] results = new String[2];
        results[0] = String.valueOf(employees.size());
        results[1] = String.valueOf((stop - start) / 1000000);
        return results;
    }
}