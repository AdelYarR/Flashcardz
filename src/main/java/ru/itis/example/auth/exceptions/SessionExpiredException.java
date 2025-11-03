package ru.itis.example.auth.exceptions;

public class SessionExpiredException extends SessionException {
    public SessionExpiredException(String message) {
        super(message);
    }
}
