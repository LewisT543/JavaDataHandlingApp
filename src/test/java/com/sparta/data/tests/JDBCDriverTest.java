package com.sparta.data.tests;

import com.sparta.data.models.utils.JDBCDriver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCDriverTest {
    @Test
    public void initialiseSQLiteDBTest() {
        JDBCDriver.initialiseDb("jdbc:sqlite:employees.db");
        File file = new File("employees.db");
        assertTrue(file.exists());
    }

    @Test
    public void initialiseMySQLDBTest() {
        String dbConnection = "jdbc:mysql://localhost:3306/employees?rewriteBatchedStatements=true";
        String dbName = "employees";
        JDBCDriver.initialiseDb(dbConnection);
        try (Connection conn = DriverManager.getConnection(dbConnection, "root", "123xyz")) {
            ResultSet rs = conn.getMetaData().getCatalogs();
            String catalogs = rs.getString(1);
            while (rs.next()) {
                if (dbName.equals(catalogs))
                    assertEquals(dbName, catalogs);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
}
