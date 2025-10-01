package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.config.DatabaseConfig;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.user.repositories.impl.UserRepositoryJdbcImpl;
import ru.itis.example.user.services.UserService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserLogServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private Connection connection;
    private UserService userService;

    @Override
    public void init() {
        try {
            connection = DatabaseConfig.getConnection();

            UserRepository userRepository = new UserRepositoryJdbcImpl(connection);
            userService = new UserService(userRepository);
        } catch (RuntimeException err) {
            throw new RuntimeException(err);
        } catch (SQLException err) {
            throw new RuntimeException("failed to connect to sql: " + err);
        }

        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("user_name");
        String password = request.getParameter("user_password");
        logger.info("Username and password parameters received.");

        try {
            User user = userService.logUser(name, password);
            request.setAttribute("user_id", user.id());
            request.setAttribute("user_name", user.name());
            request.getRequestDispatcher("group-menu.jsp").forward(request, response);
        } catch (RuntimeException err) {
            request.getRequestDispatcher("log.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException err) {
            logger.error("Failed to close connection: " + err);
        }
    }
}
