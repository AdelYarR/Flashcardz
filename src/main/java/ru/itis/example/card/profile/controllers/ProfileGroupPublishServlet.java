package ru.itis.example.card.profile.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/profile/publish-group")
public class ProfileGroupPublishServlet extends BaseCardGroupServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            cardGroupService.publishByGroupId(cardGroupId);
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
