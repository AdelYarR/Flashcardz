package ru.itis.example.options.exceptions;

public class OptionsRepositoryException extends OptionsException {
    public OptionsRepositoryException(String message) {
        super(message);
    }

    public OptionsRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
