package ru.itis.example.card.profile.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.service.CardService;
import ru.itis.example.card.repository.CardRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/cards")
public class ProfileCardsServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private CardService cardService;

    @Override
    public void init() {
        CardRepository cardRepository = (CardRepository) getServletContext().getAttribute("card_repository");
        cardService = new CardService(cardRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            List<Card> cards = cardService.getCardsByGroupId(cardGroupId);
            request.setAttribute("cards", cards);
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
