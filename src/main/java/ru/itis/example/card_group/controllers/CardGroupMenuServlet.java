package ru.itis.example.card_group.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card_group.repositories.CardGroupRepository;
import ru.itis.example.card_group.services.CardGroupService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;

import java.io.IOException;
import java.util.List;

@WebServlet("/group-menu")
public class CardGroupMenuServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private CardGroupService cardGroupService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new CardGroupService(cardGroupRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<CardGroup> cardGroups = cardGroupService.getCardGroups();
            request.setAttribute("card_groups", cardGroups);
            request.getRequestDispatcher("/group-menu.jsp").forward(request, response);
        } catch (Exception e) {
            try {
                response.sendError(500, "Internal Server Error");
            } catch (IOException ex) {
                logger.error("Failed to send error: " + ex);
            }
//            request.getRequestDispatcher("group-menu.jsp").forward(request, response);
        }
    }
}
