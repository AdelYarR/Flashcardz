package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.dependency.DependencyContainer;
import ru.itis.example.models.User;
import ru.itis.example.user.services.UserRegistryService;
import ru.itis.example.logger.Logger;

import java.io.IOException;

public class UserRegistryServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private UserRegistryService userRegistryService;

    @Override
    public void init() {
        userRegistryService = DependencyContainer.getUserRegistryService();

        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("user_name");
        String password = request.getParameter("user_password");
        String passwordConfirm = request.getParameter("user_password_confirm");
        logger.info("Username and password parameters received.");

        try {
            User user = userRegistryService.addUser(name, password, passwordConfirm);
            request.setAttribute("user_id", user.id());
            request.setAttribute("user_name", user.name());
            request.getRequestDispatcher("group-menu.jsp").forward(request, response);
        } catch (RuntimeException err) {
            request.getRequestDispatcher("registry.jsp").forward(request, response);
        }
    }
}
