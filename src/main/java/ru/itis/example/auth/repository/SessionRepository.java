package ru.itis.example.auth.repository;

import ru.itis.example.models.Session;

import java.util.Optional;

public interface SessionRepository {
    void add(Session session);
    void update(Session session);
    Optional<Session> findBySessionId(String sessionId);
    Optional<Session> findByUserId(Long userId);
}
