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
import ru.itis.example.models.UserCardProgressWithSeconds;
import ru.itis.example.training.repository.TrainingRepository;
import ru.itis.example.training.service.TrainingService;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/training-start")
public class TrainingStartServlet extends HttpServlet {

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
    }
}


