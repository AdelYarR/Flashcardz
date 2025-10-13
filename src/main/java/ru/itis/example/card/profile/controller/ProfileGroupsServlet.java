package ru.itis.example.card.profile.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.card.service.GroupService;
import ru.itis.example.card.repository.CardGroupRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/groups")
public class ProfileGroupsServlet extends HttpServlet {

    private final Logger logger = new Logger(this.getClass().getName());
    private GroupService cardGroupService;
    private SessionService sessionService;

    @Override
    public void init() {
        CardGroupRepository cardGroupRepository = (CardGroupRepository) getServletContext().getAttribute("card_group_repository");
        cardGroupService = new GroupService(cardGroupRepository);
        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

            Long authorId = sessionService.getUserIdBySessionId(sessionId);
            logger.info("Got author id from session: " + authorId);

            List<CardGroup> cardGroups = cardGroupService.getCardGroupsByAuthorId(authorId);
            request.setAttribute("card_groups", cardGroups);
            request.getRequestDispatcher("/profile-groups.jsp").forward(request, response);
        } catch (Exception e) {
            try {
                response.sendError(500, "Internal Server Error");
            } catch (IOException ex) {
                logger.error("Failed to send error: " + ex);
            }
        }
    }
}
