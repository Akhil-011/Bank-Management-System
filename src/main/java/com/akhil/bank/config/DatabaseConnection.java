package com.akhil.bank.config;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error loading properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() {

        try {

            return DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password"));

        } catch (SQLException e) {
    e.printStackTrace();
    throw new RuntimeException("Database Connection Failed", e);
}
    }
}
