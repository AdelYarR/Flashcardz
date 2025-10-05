package ru.itis.example.config;

import ru.itis.example.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static Logger logger = new Logger(DatabaseConfig.class.getName());

    static {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL driver loaded: " + org.postgresql.Driver.getVersion());
        } catch (ClassNotFoundException err) {
            logger.error("Failed to find PostgreSQL JDBC Driver: " + err);
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
