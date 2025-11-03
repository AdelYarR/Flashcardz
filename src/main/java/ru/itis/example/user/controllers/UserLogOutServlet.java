package ru.itis.example.user.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.logger.Logger;
import ru.itis.example.user.repository.UserRepository;
import ru.itis.example.user.service.UserService;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;

@WebServlet("/log-out")
public class UserLogOutServlet extends HttpServlet {

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

        sessionService.removeBySessionId(sessionId);
        Cookie cookie = new Cookie("session_id", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.sendRedirect(request.getContextPath() + "/");
    }
}
