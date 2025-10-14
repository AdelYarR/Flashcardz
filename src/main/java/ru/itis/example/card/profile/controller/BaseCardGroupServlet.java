package ru.itis.example.card.profile.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.ValidationException;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.repository.CardGroupRepository;
import ru.itis.example.card.repository.CardRepository;
import ru.itis.example.card.service.CardService;
import ru.itis.example.card.service.GroupService;
import ru.itis.example.logger.Logger;
import ru.itis.example.util.CookieHelper;

public class BaseCardGroupServlet extends HttpServlet {

    protected Logger logger;
    protected GroupService cardGroupService;
    protected CardService cardService;
    protected SessionService sessionService;

    @Override
    public void init() {
        logger = new Logger(this.getClass().getName());

        ServletContext context = getServletContext();

        CardGroupRepository cardGroupRepository = (CardGroupRepository) context.getAttribute("card_group_repository");
        CardRepository cardRepository = (CardRepository) context.getAttribute("card_repository");
        SessionRepository sessionRepository = (SessionRepository) context.getAttribute("session_repository");

        cardGroupService = new GroupService(cardGroupRepository);
        cardService = new CardService(cardRepository);
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }

    protected Long getUserIdFromCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");
        return sessionService.getUserIdBySessionId(sessionId);
    }

    protected void validateQuestionsAnswers(String[] questions, String[] answers) throws ValidationException {
        if (questions == null || answers == null || questions.length != answers.length) {
            throw new ValidationException("invalid questions and answers data");
        }
    }
}
