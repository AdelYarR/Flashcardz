package ru.itis.example.card.exceptions;

public class GroupException extends RuntimeException {
    public GroupException(String message) {
        super(message);
    }

    public GroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
