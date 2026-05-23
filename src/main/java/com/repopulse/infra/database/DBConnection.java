package com.repopulse.infra.database;

import com.repopulse.infra.config.AppConfig;
import com.repopulse.infra.exception.DataAccessException;

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
            throw new DataAccessException("Database connection failed. Check `db.url`, `db.username`, `db.password` in `config.properties`.", e);
        }
        return connection;
    }
}
