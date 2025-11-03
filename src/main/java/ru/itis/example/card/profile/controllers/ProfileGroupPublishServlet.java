package ru.itis.example.card.profile.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.GroupException;
import ru.itis.example.card.exceptions.GroupRepositoryException;

import java.io.IOException;

@WebServlet("/profile/publish-group")
public class ProfileGroupPublishServlet extends BaseCardGroupServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            cardGroupService.publishByGroupId(cardGroupId);
            response.sendRedirect(getServletContext().getContextPath() + "/profile/groups");
        } catch (GroupRepositoryException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error: " + e);
        } catch (GroupException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request: " + e);
        } catch (Exception e) {
            logger.error("Unexpected error: " + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e);
        }
    }
}
