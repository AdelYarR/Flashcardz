package ru.itis.example.card.profile.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.repository.CardGroupRepository;
import ru.itis.example.card.repository.CardRepository;
import ru.itis.example.card.service.CardService;
import ru.itis.example.card.service.GroupService;
import ru.itis.example.logger.Logger;

import java.io.IOException;

@WebServlet("/profile/delete-group")
public class ProfileGroupDeleteServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private GroupService cardGroupService;
    private CardService cardService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new GroupService(cardGroupRepository);
        CardRepository cardRepository = (CardRepository) getServletContext().getAttribute("card_repository");
        cardService = new CardService(cardRepository);
        logger.info("Successful initialization.");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            cardService.removeAllByGroupId(cardGroupId);
            cardGroupService.removeByGroupId(cardGroupId);

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
