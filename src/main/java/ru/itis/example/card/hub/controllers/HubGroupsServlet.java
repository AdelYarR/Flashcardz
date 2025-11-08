package ru.itis.example.card.hub.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.GroupException;
import ru.itis.example.card.exceptions.GroupRepositoryException;
import ru.itis.example.card.repositories.CardGroupRepository;
import ru.itis.example.card.services.GroupService;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;

import java.io.IOException;
import java.util.List;

@WebServlet("/hub/groups")
public class HubGroupsServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private GroupService cardGroupService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new GroupService(cardGroupRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<CardGroup> cardGroups = cardGroupService.getCardGroups();

            String inputText = request.getParameter("input_text");
            cardGroups = cardGroupService.processGroupsByInput(cardGroups, inputText);

            String pageParameter = request.getParameter("page");
            int page = (pageParameter != null) ? Integer.parseInt(pageParameter) : 1;
            int totalPages = cardGroupService.getTotalPages(cardGroups);
            List<CardGroup> pagedCardGroups = cardGroupService.getPagedSublistOfGroups(cardGroups, page);

            request.setAttribute("page", page);
            request.setAttribute("total_pages", totalPages);
            request.setAttribute("card_groups", pagedCardGroups);
            request.getRequestDispatcher("/hub-groups.jsp").forward(request, response);
        } catch (GroupRepositoryException e) {
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }
}
