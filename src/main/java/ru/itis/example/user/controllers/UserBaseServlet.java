package ru.itis.example.user.controllers;

import jakarta.servlet.http.HttpServlet;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.logger.Logger;
import ru.itis.example.options.repository.OptionsRepository;
import ru.itis.example.options.service.OptionsService;
import ru.itis.example.user.repository.UserRepository;
import ru.itis.example.user.service.UserService;

public abstract class UserBaseServlet extends HttpServlet {

    protected final Logger logger = new Logger(this.getClass().getName());
    protected UserService userService;
    protected SessionService sessionService;
    protected OptionsService optionsService;

    @Override
    public void init() {
        UserRepository userRepository = (UserRepository) getServletContext().getAttribute("user_repository");
        userService = new UserService(userRepository);

        SessionRepository sessionRepository = (SessionRepository) getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);

        OptionsRepository optionsRepository = (OptionsRepository) getServletContext().getAttribute("options_repository");
        optionsService = new OptionsService(optionsRepository);

        logger.info("Successful initialization.");
    }
}
