package ru.itis.example.auth.exceptions;

public class SessionNotFoundException extends SessionException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
