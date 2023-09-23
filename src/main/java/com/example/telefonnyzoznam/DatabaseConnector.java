package com.example.telefonnyzoznam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {


    private static final String URL = "jdbc:postgresql://localhost:5432/TelefonnyZoznam";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private static DatabaseConnector instance;
    private Connection connection;

    private DatabaseConnector() {
        try {

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection = null;
        }
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
