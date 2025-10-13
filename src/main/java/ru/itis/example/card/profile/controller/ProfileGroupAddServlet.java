package ru.itis.example.card.profile.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.service.CardService;
import ru.itis.example.card.service.GroupService;
import ru.itis.example.card.repository.CardGroupRepository;
import ru.itis.example.card.repository.CardRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;

@WebServlet("/profile/add-group")
public class ProfileGroupAddServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private GroupService cardGroupService;
    private CardService cardService;
    private SessionService sessionService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new GroupService(cardGroupRepository);
        CardRepository cardRepository = (CardRepository) getServletContext().getAttribute("card_repository");
        cardService = new CardService(cardRepository);
        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/profile-add-group.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            Long authorId = sessionService.getUserIdBySessionId(sessionId);
            logger.info("Got author id from session: " + authorId);

            String cardGroupName = request.getParameter("card_group_name");

            String[] questions = request.getParameterValues("questions[]");
            String[] answers = request.getParameterValues("answers[]");

            if (questions.length != answers.length) {
                response.sendError(500, "Internal Server Error");
            }

            Long cardGroupId = cardGroupService.add(authorId, cardGroupName);
            logger.info("Card group " + cardGroupId + " was successfully added.");

            for (int i = 0; i < questions.length; i++) {
                Card card = new Card(
                        null,
                        authorId,
                        cardGroupId,
                        questions[i],
                        answers[i]
                );
                cardService.add(card);
            }

            response.sendRedirect(getServletContext().getContextPath() + "/profile/groups");
        } catch (Exception e) {
            try {
                response.sendError(500, "Internal Server Error");
            } catch (IOException ex) {
                logger.error("Failed to send error: " + ex);
            }
        }
    }
}
