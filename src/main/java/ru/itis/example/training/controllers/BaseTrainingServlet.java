package ru.itis.example.training.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.repositories.CardRepository;
import ru.itis.example.card.services.CardService;
import ru.itis.example.logger.Logger;
import ru.itis.example.training.repository.TrainingRepository;
import ru.itis.example.training.service.TrainingService;

public class BaseTrainingServlet extends HttpServlet {
    protected Logger logger;
    protected TrainingService trainingService;
    protected CardService cardService;
    protected SessionService sessionService;

    @Override
    public void init() {
        logger = new Logger(this.getClass().getName());

        ServletContext context = getServletContext();

        TrainingRepository trainingRepository = (TrainingRepository) context.getAttribute("training_repository");
        CardRepository cardRepository = (CardRepository) context.getAttribute("card_repository");
        SessionRepository sessionRepository = (SessionRepository) context.getAttribute("session_repository");

        trainingService = new TrainingService(trainingRepository);
        cardService = new CardService(cardRepository);
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }
}
