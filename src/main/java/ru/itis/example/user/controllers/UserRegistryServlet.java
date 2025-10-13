package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.user.repository.UserRepository;
import ru.itis.example.user.service.UserService;
import ru.itis.example.logger.Logger;

import java.io.IOException;

@WebServlet("/registry")
public class UserRegistryServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private UserService userService;
    private SessionService sessionService;

    @Override
    public void init() {
        UserRepository userRepository = (UserRepository) getServletContext().getAttribute("user_repository");
        userService = new UserService(userRepository);

        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);

        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("user_name");
        String password = request.getParameter("user_password");
        String passwordConfirm = request.getParameter("user_password_confirm");
        logger.info("Username and password parameters received.");

        try {
            Long userId = userService.registryUser(name, password, passwordConfirm);

            String sessionId = sessionService.add(userId);
            Cookie sessionCookie = new Cookie("session_id", sessionId);
            response.addCookie(sessionCookie);
            logger.info("Cookie with session id " + sessionId + " was successfully added.");

            response.sendRedirect("hub/groups");
        } catch (RuntimeException e) {
            response.sendError(400, "Bad Request");
        }
    }
}
