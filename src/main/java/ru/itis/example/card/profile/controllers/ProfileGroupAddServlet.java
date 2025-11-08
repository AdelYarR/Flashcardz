package ru.itis.example.card.profile.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.card.exceptions.CardException;
import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.card.exceptions.GroupException;
import ru.itis.example.card.exceptions.GroupRepositoryException;
import ru.itis.example.models.Card;

import java.io.IOException;

@WebServlet("/profile/add-group")
public class ProfileGroupAddServlet extends BaseCardGroupServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/profile-add-group.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long authorId = getUserIdFromCookies(request, response);
            logger.info("Got author id from session: " + authorId);

            String cardGroupName = request.getParameter("card_group_name");

            String[] questions = getOrDefault(request.getParameterValues("questions[]"));
            String[] answers = getOrDefault(request.getParameterValues("answers[]"));

            validateQuestionsAnswers(questions, answers);

            Long cardGroupId = cardGroupService.add(authorId, cardGroupName);
            logger.info("Card group " + cardGroupId + " was successfully added.");

            for (int i = 0; i < questions.length; i++) {
                Card card = new Card(
                        null,
                        authorId,
                        cardGroupId,
                        questions[i],
                        answers[i]
                );
                cardService.add(card);
            }

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
