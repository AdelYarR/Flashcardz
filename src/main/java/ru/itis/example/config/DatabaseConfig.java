package ru.itis.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver loaded: " + org.postgresql.Driver.getVersion());
        } catch (ClassNotFoundException err) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found: " + err);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = EnvConfig.get("DB_URL");
        String user = EnvConfig.get("DB_USER");
        String password = EnvConfig.get("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }
}
