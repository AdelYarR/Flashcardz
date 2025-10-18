package ru.itis.example.training.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.repositories.CardRepository;
import ru.itis.example.card.services.CardService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;
import ru.itis.example.models.TrainingSession;
import ru.itis.example.models.TrainingSessionCard;
import ru.itis.example.models.UserCardProgress;
import ru.itis.example.training.repository.TrainingRepository;
import ru.itis.example.training.service.TrainingService;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/training-continue")
public class TrainingContinueServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private TrainingService trainingService;
    private CardService cardService;
    private SessionService sessionService;

    @Override
    public void init() {
        ServletContext context = getServletContext();

        TrainingRepository trainingRepository = (TrainingRepository) context.getAttribute("training_repository");
        CardRepository cardRepository = (CardRepository) context.getAttribute("card_repository");
        SessionRepository sessionRepository = (SessionRepository) context.getAttribute("session_repository");

        trainingService = new TrainingService(trainingRepository);
        cardService = new CardService(cardRepository);
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
        String trainingSessionId = CookieHelper.getValueFromCookies(cookies, "training_session_id");

        if (trainingSessionId == null) {
            request.getRequestDispatcher("/sorry.jsp").forward(request, response);
        }
        logger.info("Successfully got training session id: " + trainingSessionId);

        TrainingSession trainingSession = trainingService.getByTrainingSessionId(trainingSessionId);

        Long userId = sessionService.getUserIdBySessionId(sessionId);
        Long userIdFromTraining = trainingSession.getUserId();
        if (userId != userIdFromTraining) {
            request.getRequestDispatcher("/sorry.jsp").forward(request, response);
        }

        int currentIndex = trainingSession.getCurrentIndex();
        if (currentIndex >= trainingSession.getMaxCards()) {
            response.sendRedirect(request.getContextPath() + "/hub/groups");
        }

        List<Long> trainingSessionCards = trainingService.getTrainingCardIdsByTrainingSessionId(trainingSessionId);
        Long currentCardId = trainingSessionCards.get(currentIndex);
        Card card = cardService.getCardById(currentCardId);

        request.setAttribute("card", card);
        request.getRequestDispatcher("/review-card.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
        String trainingSessionId = CookieHelper.getValueFromCookies(cookies, "training_session_id");

        if (trainingSessionId == null) {
            request.getRequestDispatcher("/sorry.jsp").forward(request, response);
        }

        TrainingSession trainingSession = trainingService.getByTrainingSessionId(trainingSessionId);

        Long userId = sessionService.getUserIdBySessionId(sessionId);
        Long userIdFromTraining = trainingSession.getUserId();
        if (userId != userIdFromTraining) {
            request.getRequestDispatcher("/sorry.jsp").forward(request, response);
        }

        String action = request.getParameter("action");
        Long cardId = Long.valueOf(request.getParameter("card_id"));

        trainingService.addUserCardProgress(userId, cardId, action);
        trainingService.updateIndexOfTrainingSession(trainingSession);

        response.sendRedirect(request.getContextPath() + "/training-continue");
    }
}