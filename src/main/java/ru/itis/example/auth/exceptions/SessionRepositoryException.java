package ru.itis.example.auth.exceptions;

public class SessionRepositoryException extends SessionException {
    public SessionRepositoryException(String message) {
        super(message);
    }

    public SessionRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
