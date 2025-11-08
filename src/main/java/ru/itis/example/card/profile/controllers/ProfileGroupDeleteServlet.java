package ru.itis.example.card.profile.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.CardException;
import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.card.exceptions.GroupException;
import ru.itis.example.card.exceptions.GroupRepositoryException;

import java.io.IOException;

@WebServlet("/profile/delete-group")
public class ProfileGroupDeleteServlet extends BaseCardGroupServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long cardGroupId = Long.valueOf(request.getParameter("card_group_id"));
            cardService.removeAllByGroupId(cardGroupId);
            cardGroupService.removeByGroupId(cardGroupId);

            response.sendRedirect(getServletContext().getContextPath() + "/profile/groups");
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
