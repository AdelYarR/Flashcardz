package ru.itis.example.auth.service;

import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Session;

import java.util.Optional;

public class SessionService {
    private final Logger logger = new Logger(this.getClass().getName());
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String add(Long userId) {
        Session session = new Session(userId);

        if (findByUserId(userId).isPresent()) {
            sessionRepository.update(session);
            logger.info("Session " + session.getSessionId() + " was successfully added.");
        } else {
            sessionRepository.add(session);
            logger.info("Session " + session.getSessionId() + " was successfully updated.");
        }

        return session.getSessionId();
    }

    public boolean isValid(String sessionId) {
        Optional<Session> optionalSession = findBySessionId(sessionId);
        if (optionalSession.isEmpty()) {
            return false;
        }
        Session session = optionalSession.get();

        return session.getValidSession();
    }

    public Long getUserIdBySessionId(String sessionId) {
        Optional<Session> optionalSession = findBySessionId(sessionId);
        if (optionalSession.isEmpty()) {
            throw new RuntimeException("failed to get session by session id");
        }
        Session session = optionalSession.get();

        return session.getUserId();
    }

    public void removeBySessionId(String sessionId) {
        sessionRepository.removeBySessionId(sessionId);
    }

    public Optional<Session> findBySessionId(String sessionId) {
        return sessionRepository.findBySessionId(sessionId);
    }

    public Optional<Session> findByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }
}
