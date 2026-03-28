package com.repopulse.database;

import com.repopulse.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                String url = AppConfig.get("db.url");
                String username = AppConfig.get("db.username");
                String password = AppConfig.get("db.password");

                connection = DriverManager.getConnection(url, username, password);
            }
        } catch(Exception e) {
            throw new RuntimeException("DB Connection failed", e);
        }
        return connection;
    }
}
