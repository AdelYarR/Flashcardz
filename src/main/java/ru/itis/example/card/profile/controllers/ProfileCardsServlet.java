package ru.itis.example.card.profile.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.CardException;
import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.models.Card;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/cards")
public class ProfileCardsServlet extends BaseCardGroupServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);
            request.setAttribute("cards", cards);
            request.setAttribute("card_group_id", cardGroupId);
            request.getRequestDispatcher("/hub-cards.jsp").forward(request, response);
        } catch (CardRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (CardException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }
}
