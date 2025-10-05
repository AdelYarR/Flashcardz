package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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

@WebServlet(value = "/log", loadOnStartup = 1)
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
        } catch (RuntimeException e) {
            logger.error("Failed: " + e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            logger.error("Failed to create connection to SQL: " + e);
            throw new RuntimeException("failed to connect to sql: " + e);
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
        } catch (RuntimeException e) {
            response.sendError(400, "Bad Request");
//            request.getRequestDispatcher("log.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection: " + e);
        }
    }
}
