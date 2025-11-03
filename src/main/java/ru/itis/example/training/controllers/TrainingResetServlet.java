package ru.itis.example.training.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;

@WebServlet("/training-reset")
public class TrainingResetServlet extends BaseTrainingServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

        Long userId = sessionService.getUserIdBySessionId(sessionId);
        Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));

        trainingService.deleteUserCardProgressByUserAndGroupId(userId, cardGroupId);
        response.sendRedirect(request.getContextPath() + "/hub/cards?card_group_id=" + cardGroupId);
    }
}
