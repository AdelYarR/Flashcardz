package ru.itis.example.card.profile.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.models.Card;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/cards")
public class ProfileCardsServlet extends BaseCardGroupServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);
            request.setAttribute("cards", cards);
            request.setAttribute("card_group_id", cardGroupId);
            request.getRequestDispatcher("/hub-cards.jsp").forward(request, response);
        } catch (Exception e) {
            try {
                response.sendError(500, "Internal Server Error");
            } catch (IOException ex) {
                logger.error("Failed to send error: " + ex);
            }
        }
    }
}
