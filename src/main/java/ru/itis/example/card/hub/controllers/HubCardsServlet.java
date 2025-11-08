package ru.itis.example.card.hub.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.card.repositories.CardRepository;
import ru.itis.example.card.services.CardService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;

import java.io.IOException;
import java.util.List;

@WebServlet("/hub/cards")
public class HubCardsServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private CardService cardService;

    @Override
    public void init() {
        CardRepository cardRepository = (CardRepository) getServletContext().getAttribute("card_repository");
        cardService = new CardService(cardRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);
            request.setAttribute("cards", cards);
            request.setAttribute("card_group_id", cardGroupId);
            request.getRequestDispatcher("/hub-cards.jsp").forward(request, response);
        } catch (CardRepositoryException e) {
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }
}
