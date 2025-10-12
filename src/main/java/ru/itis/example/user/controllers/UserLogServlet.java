package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.AuthService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.user.services.UserService;

import java.io.IOException;

@WebServlet("/log")
public class UserLogServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private UserService userService;
    private AuthService authService;

    @Override
    public void init() {
        UserRepository userRepository = (UserRepository) getServletContext().getAttribute("user_repository");
        userService = new UserService(userRepository);

        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        authService = new AuthService(sessionRepository);

        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("user_name");
        String password = request.getParameter("user_password");
        logger.info("Username and password parameters received.");

        try {
            Long userId = userService.logUser(name, password);

            String sessionId = authService.add(userId);
            Cookie sessionCookie = new Cookie("session_id", sessionId);
            response.addCookie(sessionCookie);
            logger.info("Cookie with session id " + sessionId + " was successfully added.");

            response.sendRedirect("group-menu");
        } catch (RuntimeException e) {
            response.sendError(400, "Bad Request");}
    }
}
