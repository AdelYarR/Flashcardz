package ru.itis.example.training.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.exceptions.SessionException;
import ru.itis.example.auth.exceptions.SessionRepositoryException;
import ru.itis.example.card.exceptions.CardException;
import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.models.Card;
import ru.itis.example.models.TrainingSession;
import ru.itis.example.models.UserCardProgressWithSeconds;
import ru.itis.example.training.exceptions.InvalidActionException;
import ru.itis.example.training.exceptions.TrainingException;
import ru.itis.example.training.exceptions.TrainingRepositoryException;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/training-start")
public class TrainingStartServlet extends BaseTrainingServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            Long userId = sessionService.getUserIdBySessionId(sessionId);
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));

            trainingService.deleteOldTrainingSession(userId, cardGroupId);

            List<Card> allCards = cardService.getCardsByGroupId(cardGroupId);
            List<UserCardProgressWithSeconds> userCardProgressWithSeconds = trainingService.getUserCardProgresses(userId, cardGroupId);

            List<Long> trainingCardIds = trainingService.getTrainingCardIdsByAllCardsAndProgresses(allCards, userCardProgressWithSeconds);

            TrainingSession trainingSession = new TrainingSession(userId, cardGroupId, trainingCardIds.size());
            String trainingSessionId = trainingSession.getSessionId();
            trainingService.addTrainingSession(trainingSession);
            Cookie sessionCookie = new Cookie("training_session_id", trainingSessionId);
            response.addCookie(sessionCookie);
            logger.info("Cookie with training session id " + trainingSessionId + " was successfully added.");

            trainingService.addTrainingCards(trainingSessionId, trainingCardIds);

            response.sendRedirect(request.getContextPath() + "/training-continue");
        } catch (TrainingRepositoryException | CardRepositoryException | SessionRepositoryException e) {
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Internal Server Error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (TrainingException | CardException | SessionException e) {
            request.setAttribute("message", HttpServletResponse.SC_BAD_REQUEST + " Bad Request: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }
}