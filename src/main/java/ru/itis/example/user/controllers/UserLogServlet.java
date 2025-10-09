package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.user.services.UserService;

import java.io.IOException;

@WebServlet("/log")
public class UserLogServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private UserService userService;

    @Override
    public void init() {
        UserRepository userRepository = (UserRepository) getServletContext().getAttribute("user_repository");
        userService = new UserService(userRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("user_name");
        String password = request.getParameter("user_password");
        logger.info("Username and password parameters received.");

        try {
            User user = userService.logUser(name, password);

//            session.add(user.id());

            response.sendRedirect("group-menu");
        } catch (RuntimeException e) {
            response.sendError(400, "Bad Request");}
    }
}
