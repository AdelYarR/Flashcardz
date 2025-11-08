package ru.itis.example.training.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.exceptions.SessionException;
import ru.itis.example.auth.exceptions.SessionRepositoryException;
import ru.itis.example.training.exceptions.InvalidActionException;
import ru.itis.example.training.exceptions.TrainingException;
import ru.itis.example.training.exceptions.TrainingRepositoryException;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;

@WebServlet("/training-reset")
public class TrainingResetServlet extends BaseTrainingServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            Long userId = sessionService.getUserIdBySessionId(sessionId);
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));

            trainingService.deleteUserCardProgressByUserAndGroupId(userId, cardGroupId);
            response.sendRedirect(request.getContextPath() + "/hub/cards?card_group_id=" + cardGroupId);
        } catch (TrainingRepositoryException | SessionRepositoryException e) {
            request.setAttribute("message", HttpServletResponse.SC_BAD_REQUEST + " Bad Request: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (TrainingException | SessionException e) {
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Internal Server Error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }
}
