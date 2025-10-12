package ru.itis.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Session {
    private static final int STANDARD_MAX_INACTIVE = 1800;

    private String sessionId;
    private Long userId;
    // Длительность сессии в секундах
    private Integer maxInactive;
    // Время создания/возобновления сессии в Unix-time секундах
    private Long lastAccess;

    public Session(Long userId) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.maxInactive = STANDARD_MAX_INACTIVE;
        this.lastAccess = System.currentTimeMillis() / 1000;
    }

    public boolean getValidSession() {
        long sessionMaxUnixTime = lastAccess + maxInactive;
        long currentUnixTime = System.currentTimeMillis() / 1000;

        return currentUnixTime < sessionMaxUnixTime;
    }
}
