package ru.itis.example.models;

import java.util.UUID;

public class TrainingSession {
    private static final int STANDARD_MAX_INACTIVE = 43200;

    private String sessionId;
    private Long userId;
    private Long cardGroupId;
    private Integer currentIndex;
    private Integer maxCards;
    // Длительность сессии в секундах
    private Integer maxInactive;
    // Время создания/возобновления сессии в Unix-time секундах
    private Long lastAccess;

    public TrainingSession(String sessionId, Long userId, Long cardGroupId, Integer currentIndex, Integer maxCards, Integer maxInactive, Long lastAccess) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.cardGroupId = cardGroupId;
        this.currentIndex = currentIndex;
        this.maxCards = maxCards;
        this.maxInactive = maxInactive;
        this.lastAccess = lastAccess;
    }

    public TrainingSession(Long userId, Long cardGroupId, Integer maxCards) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.cardGroupId = cardGroupId;
        this.currentIndex = 0;
        this.maxCards = maxCards;
        this.maxInactive = STANDARD_MAX_INACTIVE;
        this.lastAccess = System.currentTimeMillis() / 1000;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardGroupId() {
        return cardGroupId;
    }

    public void setCardGroupId(Long cardGroupId) {
        this.cardGroupId = cardGroupId;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Integer getMaxCards() {
        return maxCards;
    }

    public void setMaxCards(Integer maxCards) {
        this.maxCards = maxCards;
    }

    public Integer getMaxInactive() {
        return maxInactive;
    }

    public void setMaxInactive(Integer maxInactive) {
        this.maxInactive = maxInactive;
    }

    public Long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Long lastAccess) {
        this.lastAccess = lastAccess;
    }
}
