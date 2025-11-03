package ru.itis.example.user.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.exceptions.SessionRepositoryException;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;

@WebServlet("/log-out")
public class UserLogOutServlet extends UserBaseServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            sessionService.removeBySessionId(sessionId);
            Cookie cookie = new Cookie("session_id", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (SessionRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }
}
