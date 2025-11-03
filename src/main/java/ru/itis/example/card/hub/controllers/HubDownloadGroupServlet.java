package ru.itis.example.card.hub.controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.repositories.CardGroupRepository;
import ru.itis.example.card.services.GroupService;
import ru.itis.example.logger.Logger;

public class HubDownloadGroupServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private GroupService cardGroupService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new GroupService(cardGroupRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
