package ru.itis.example.training.exceptions;

public class TrainingRepositoryException extends TrainingException {
    public TrainingRepositoryException(String message) {
        super(message);
    }

    public TrainingRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
