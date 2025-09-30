package ru.itis.example.dependency;

import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.user.repositories.impl.UserRepositoryJdbcImpl;
import ru.itis.example.user.services.UserRegistryService;
import ru.itis.example.util.TxtEnvParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DependencyContainer {

    private static Connection connection;
    private static UserRepository userRepository;
    private static UserRegistryService userRegistryService;

    static {
        try {
            Class.forName("org.postgresql.Driver");

            String url = TxtEnvParser.getEnv("DB_URL");
            String user = TxtEnvParser.getEnv("DB_USER");
            String password = TxtEnvParser.getEnv("DB_PASSWORD");

            connection = DriverManager.getConnection(url, user, password);

            userRepository = new UserRepositoryJdbcImpl(connection);
            userRegistryService = new UserRegistryService(userRepository);
        } catch (RuntimeException err) {
            throw new RuntimeException(err);
        } catch (SQLException err) {
            throw new RuntimeException("failed to connect to sql: " + err);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public static UserRegistryService getUserRegistryService() {
        return userRegistryService;
    }
}
