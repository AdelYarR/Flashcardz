package ru.itis.example.models;


public class UserCardProgressWithSeconds {
    private Long id;
    private Long userId;
    private Long cardId;
    private Integer maxSeconds;
    // Время создания/возобновления сессии в Unix-time секундах
    private Long lastAccess;

    public UserCardProgressWithSeconds(Long id, Long userId, Long cardId, Integer maxSeconds, Long lastAccess) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.maxSeconds = maxSeconds;
        this.lastAccess = lastAccess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public boolean isExpired() {
        long sessionMaxUnixTime = lastAccess + maxSeconds;
        long currentUnixTime = System.currentTimeMillis() / 1000;

        return currentUnixTime < sessionMaxUnixTime;
    }
}
