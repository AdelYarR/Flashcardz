package ru.itis.example.card.profile.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.models.Card;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/update-group")
public class ProfileGroupUpdateServlet extends BaseCardGroupServlet {

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
            Long authorId = getUserIdFromCookies(request, response);
            logger.info("Got author id from session: " + authorId);

            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            String newCardGroupName = request.getParameter("card_group_name");

            cardGroupService.updateCardGroupName(cardGroupId, newCardGroupName);

            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);

            String[] questions = getOrDefault(request.getParameterValues("questions[]"));
            String[] answers = getOrDefault(request.getParameterValues("answers[]"));
            String[] cardIds = getOrDefault(request.getParameterValues("card_ids[]"));

            validateQuestionsAnswers(questions, answers);
            logger.info("Validated successfully");

            List<Card> newCards = cardService.buildCardsFromParams(authorId, cardGroupId, cardIds, questions, answers);
            cardService.processCardUpdate(cards, newCards);

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
