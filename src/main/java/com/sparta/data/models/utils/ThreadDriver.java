package com.sparta.data.models.utils;

import com.sparta.data.models.WriteableEmployee;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class ThreadDriver extends Thread {
    private int threadID;
    private List<WriteableEmployee> employees;
    private static final HashMap<String, String> SQL_QUERIES = new HashMap<>() {{
        put("sqlInsert", "INSERT INTO employees (prefix_id, f_name, mid_initial, l_name, gender_id, " +
                "email, date_of_birth, date_of_joining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
    }};

    public ThreadDriver(int threadID, List<WriteableEmployee> employees) {
        this.threadID = threadID;
        this.employees = employees;
    }

    @Override
    public void run() {
        writeToDB();
    }

    public synchronized void writeToDB() {
        String dbConnection = "jdbc:mysql://localhost:3306/employees";
        String user = "root";
        String pass = "123xyz";
        PreparedStatement statement = null;
        try (Connection conn = DriverManager.getConnection(dbConnection, user, pass)) {
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
                    j = 0;
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            close(statement);
        }
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
