package ru.itis.example.options.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import ru.itis.example.auth.exceptions.SessionException;
import ru.itis.example.auth.exceptions.SessionRepositoryException;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.DurationWrapper;
import ru.itis.example.models.UserCardSettings;
import ru.itis.example.options.exceptions.OptionsException;
import ru.itis.example.options.exceptions.OptionsRepositoryException;
import ru.itis.example.options.repository.OptionsRepository;
import ru.itis.example.options.service.OptionsService;
import ru.itis.example.training.exceptions.TrainingNotFoundException;
import ru.itis.example.training.exceptions.TrainingRepositoryException;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.time.Duration;

@WebServlet("/options")
public class OptionsServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private OptionsService optionsService;
    private SessionService sessionService;

    @Override
    public void init() {
        OptionsRepository optionsRepository = (OptionsRepository) getServletContext().getAttribute("options_repository");
        optionsService = new OptionsService(optionsRepository);

        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);

        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
            Long userId = sessionService.getUserIdBySessionId(sessionId);

            UserCardSettings settings = optionsService.getUserCardSettingsByUserId(userId);

            request.setAttribute("very_easy",
                    new DurationWrapper(
                            Duration.ofSeconds(settings.getVeryEasySeconds()),
                            "very_easy", "Очень легко"));
            request.setAttribute("easy",
                    new DurationWrapper(
                            Duration.ofSeconds(settings.getEasySeconds()),
                            "easy", "Легко"));
            request.setAttribute("medium",
                    new DurationWrapper(
                            Duration.ofSeconds(settings.getMediumSeconds()),
                            "medium", "Средне"));
            request.setAttribute("hard",
                    new DurationWrapper(
                            Duration.ofSeconds(settings.getHardSeconds()),
                            "hard", "Сложно"));

            request.getRequestDispatcher("/options.jsp").forward(request, response);
        } catch (OptionsRepositoryException | SessionRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (OptionsException | SessionException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
            Long userId = sessionService.getUserIdBySessionId(sessionId);

            UserCardSettings settings = getUserCardSettingsFromRequest(request, userId);

            optionsService.update(settings);

            response.sendRedirect(request.getContextPath() + "/options");
        } catch (OptionsRepositoryException | SessionRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (OptionsException | SessionException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }

    private UserCardSettings getUserCardSettingsFromRequest(HttpServletRequest request, Long userId) {
        int veryEasySeconds = getSecondsFromRequest(request, "very_easy");
        int easySeconds = getSecondsFromRequest(request, "easy");
        int mediumSeconds = getSecondsFromRequest(request, "medium");
        int hardSeconds = getSecondsFromRequest(request, "hard");

        return new UserCardSettings(
                null,
                userId,
                veryEasySeconds,
                easySeconds,
                mediumSeconds,
                hardSeconds
        );
    }

    private int getSecondsFromRequest(HttpServletRequest request, String type) {
        int days = Integer.parseInt(request.getParameter(type + "_days"));
        int hours = Integer.parseInt(request.getParameter(type + "_hours"));
        int minutes = Integer.parseInt(request.getParameter(type + "_minutes"));
        int seconds = Integer.parseInt(request.getParameter(type + "_seconds"));

        return ((days * 24 + hours) * 60 + minutes) * 60 + seconds;
    }
}
