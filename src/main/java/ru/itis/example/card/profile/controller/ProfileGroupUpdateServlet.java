package ru.itis.example.card.profile.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.repository.CardGroupRepository;
import ru.itis.example.card.repository.CardRepository;
import ru.itis.example.card.service.CardService;
import ru.itis.example.card.service.GroupService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@WebServlet("/profile/update-group")
public class ProfileGroupUpdateServlet extends HttpServlet {

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
        Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
        String cardGroupName = request.getParameter("card_group_name");

        List<Card> cards = cardService.getCardsByGroupId(cardGroupId);

        request.setAttribute("card_group_id", cardGroupId);
        request.setAttribute("card_group_name", cardGroupName);
        request.setAttribute("cards", cards);

        request.getRequestDispatcher("/profile-update-group.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            Long authorId = sessionService.getUserIdBySessionId(sessionId);
            logger.info("Got author id from session: " + authorId);

            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            String newCardGroupName = request.getParameter("card_group_name");

            cardGroupService.updateCardGroupName(cardGroupId, newCardGroupName);

            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);
            Map<Long, Card> cardsMap = cards.stream()
                    .collect(Collectors.toMap(Card::getId, Function.identity()));

            String[] questions = request.getParameterValues("questions[]");
            String[] answers = request.getParameterValues("answers[]");
            String[] cardIds = request.getParameterValues("card_ids[]");

            if (questions.length != answers.length) {
                response.sendError(500, "Internal Server Error");
            }

            logger.info(questions.length + " " + cardIds.length);

            for (int i = 0; i < questions.length; i++) {
                Long cardId = null;
                if (i < cardIds.length) {
                    cardId = Long.valueOf(cardIds[i]);
                }

                Card card = new Card(
                        cardId,
                        authorId,
                        cardGroupId,
                        questions[i],
                        answers[i]
                );

                if (cardsMap.containsKey(cardId)) {
                    cardService.update(card);
                } else {
                    cardService.add(card);
                }
                cardsMap.remove(cardId);
            }

            cardsMap.forEach((key, val) -> cardService.removeByCardId(key));

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
