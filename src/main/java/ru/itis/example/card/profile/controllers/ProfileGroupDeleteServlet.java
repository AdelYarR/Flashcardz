package ru.itis.example.card.profile.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/profile/delete-group")
public class ProfileGroupDeleteServlet extends BaseCardGroupServlet {

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
