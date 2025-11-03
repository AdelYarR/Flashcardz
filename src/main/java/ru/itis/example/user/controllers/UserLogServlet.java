package ru.itis.example.user.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.user.exceptions.UserAuthenticationException;
import ru.itis.example.user.exceptions.UserRegistrationException;
import ru.itis.example.user.exceptions.UserRepositoryException;
import ru.itis.example.user.exceptions.UserValidationException;

import java.io.IOException;

@WebServlet("/log")
public class UserLogServlet extends UserBaseServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("user_name");
            String password = request.getParameter("user_password");
            logger.info("Username and password parameters received.");

            Long userId = userService.logUser(name, password);

            String sessionId = sessionService.add(userId);
            Cookie sessionCookie = new Cookie("session_id", sessionId);
            response.addCookie(sessionCookie);
            logger.info("Cookie with session id " + sessionId + " was successfully added.");

            response.sendRedirect("hub/groups");
        } catch (UserAuthenticationException | UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request: " + e);
        } catch (UserRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }
}
