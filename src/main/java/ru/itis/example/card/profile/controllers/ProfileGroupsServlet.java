package ru.itis.example.card.profile.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.GroupException;
import ru.itis.example.card.exceptions.GroupRepositoryException;
import ru.itis.example.models.CardGroup;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/groups")
public class ProfileGroupsServlet extends BaseCardGroupServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long authorId = getUserIdFromCookies(request, response);
            logger.info("Got author id from session: " + authorId);

            List<CardGroup> cardGroups = cardGroupService.getCardGroupsByAuthorId(authorId);

            String pageParameter = request.getParameter("page");
            int page = (pageParameter != null) ? Integer.parseInt(pageParameter) : 1;
            int totalPages = cardGroupService.getTotalPages(cardGroups);
            List<CardGroup> pagedCardGroups = cardGroupService.getPagedSublistOfGroups(cardGroups, page);

            request.setAttribute("page", page);
            request.setAttribute("total_pages", totalPages);
            request.setAttribute("card_groups", pagedCardGroups);
            request.getRequestDispatcher("/profile-groups.jsp").forward(request, response);
        } catch (GroupRepositoryException e) {
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (GroupException e) {
            request.setAttribute("message", HttpServletResponse.SC_BAD_REQUEST + " Bad Request: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            request.setAttribute("message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " Unexpected error: " + e);
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }
}
