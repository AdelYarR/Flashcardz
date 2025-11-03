package ru.itis.example.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.itis.example.auth.repository.impl.SessionRepositoryJdbcImpl;
import ru.itis.example.card.repositories.impl.CardRepositoryJdbcImpl;
import ru.itis.example.card.repositories.impl.CardGroupRepositoryJdbcImpl;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.SimpleDataSource;
import ru.itis.example.options.repository.impl.OptionsRepositoryJdbcImpl;
import ru.itis.example.training.repository.impl.TrainingRepositoryJdbcImpl;
import ru.itis.example.user.repository.impl.UserRepositoryJdbcImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;


@WebListener
public class RepositoryContextListener implements ServletContextListener {

    private static final Logger logger = new Logger(RepositoryContextListener.class.getName());
    private Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("PostgreSQL driver loaded: " + org.postgresql.Driver.getVersion());

            DataSource dataSource = createDataSource();

            event.getServletContext().setAttribute(
                    "user_repository", new UserRepositoryJdbcImpl(dataSource));
            event.getServletContext().setAttribute(
                    "session_repository", new SessionRepositoryJdbcImpl(dataSource));
            event.getServletContext().setAttribute(
                    "card_group_repository", new CardGroupRepositoryJdbcImpl(dataSource));
            event.getServletContext().setAttribute(
                    "card_repository", new CardRepositoryJdbcImpl(dataSource));
            event.getServletContext().setAttribute(
                    "training_repository", new TrainingRepositoryJdbcImpl(dataSource));
            event.getServletContext().setAttribute(
                    "options_repository", new OptionsRepositoryJdbcImpl(dataSource));
            logger.info("Put repositories into servlet context.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to find PostgreSQL JDBC Driver: " + e);
            throw new RuntimeException("PostgreSQL JDBC Driver not found: " + e);
        }
    }

    private DataSource createDataSource() {
        Properties properties = new Properties();
        try {
            InputStream inStream = getClass().getResourceAsStream("/application.properties");
            properties.load(inStream);
            logger.info("Application properties are loaded.");
        } catch (IOException e) {
            logger.error("Failed to load properties: " + e);
            throw new RuntimeException("failed to load properties: " + e);
        }

        String dbUrl = properties.getProperty("DB_URL");
        String dbUser = properties.getProperty("DB_USER");
        String dbPassword = properties.getProperty("DB_PASSWORD");

        return new SimpleDataSource(dbUrl, dbUser, dbPassword);
    }
}