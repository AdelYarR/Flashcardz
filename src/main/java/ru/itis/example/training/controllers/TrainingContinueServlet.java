package ru.itis.example.training.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.exceptions.SessionRepositoryException;
import ru.itis.example.models.Card;
import ru.itis.example.models.TrainingSession;
import ru.itis.example.training.exceptions.InvalidActionException;
import ru.itis.example.training.exceptions.TrainingNotFoundException;
import ru.itis.example.training.exceptions.TrainingRepositoryException;
import ru.itis.example.user.exceptions.UserRepositoryException;
import ru.itis.example.util.CookieHelper;
import ru.itis.example.util.ResponseHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/training-continue")
public class TrainingContinueServlet extends BaseTrainingServlet {

    private static final String TRAINING_OVER_MESSAGE = "Тренировка завершена! Все карточки пройдены.";
    private static final String ACCESS_DENIED_MESSAGE = "Доступ к тренировке запрещен.";
    private static final String SESSION_EXPIRED_MESSAGE = "Сессия тренировки не найдена или истекла.";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
            String trainingSessionId = CookieHelper.getValueFromCookies(cookies, "training_session_id");

            if (trainingSessionId == null) {
                ResponseHelper.showMessage(request, response, SESSION_EXPIRED_MESSAGE);
                return;
            }
            logger.info("Successfully got training session id: " + trainingSessionId);

            TrainingSession trainingSession = trainingService.getByTrainingSessionId(trainingSessionId);

            Long userId = sessionService.getUserIdBySessionId(sessionId);
            Long userIdFromTraining = trainingSession.getUserId();

            if (userId != userIdFromTraining) {
                ResponseHelper.showMessage(request, response, ACCESS_DENIED_MESSAGE);
                return;
            }

            int currentIndex = trainingSession.getCurrentIndex();

            if (currentIndex >= trainingSession.getMaxCards()) {
                trainingService.deleteTrainingSession(trainingSessionId);
                CookieHelper.removeCookie(response, "training_session_id");
                ResponseHelper.showMessage(request, response, TRAINING_OVER_MESSAGE);
                return;
            }

            List<Long> trainingSessionCards = trainingService.getTrainingCardIdsByTrainingSessionId(trainingSessionId);
            Long currentCardId = trainingSessionCards.get(currentIndex);
            Card card = cardService.getCardById(currentCardId);

            request.setAttribute("card", card);
            request.getRequestDispatcher("/review-card.jsp").forward(request, response);
        } catch (TrainingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request: " + e);
        } catch (TrainingRepositoryException | SessionRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
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
            String trainingSessionId = CookieHelper.getValueFromCookies(cookies, "training_session_id");

            if (trainingSessionId == null) {
                ResponseHelper.showMessage(request, response, SESSION_EXPIRED_MESSAGE);
                return;
            }

            TrainingSession trainingSession = trainingService.getByTrainingSessionId(trainingSessionId);

            Long userId = sessionService.getUserIdBySessionId(sessionId);
            Long userIdFromTraining = trainingSession.getUserId();

            if (userId != userIdFromTraining) {
                ResponseHelper.showMessage(request, response, ACCESS_DENIED_MESSAGE);
                return;
            }

            String action = request.getParameter("action");
            Long cardId = Long.valueOf(request.getParameter("card_id"));

            trainingService.addUserCardProgress(userId, cardId, action);
            trainingService.updateIndexOfTrainingSession(trainingSession);

            response.sendRedirect(request.getContextPath() + "/training-continue");
        } catch (InvalidActionException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request: " + e);
        } catch (TrainingRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }
}