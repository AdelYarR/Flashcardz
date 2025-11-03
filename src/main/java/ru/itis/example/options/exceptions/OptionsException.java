package ru.itis.example.options.exceptions;

public class OptionsException extends RuntimeException {
    public OptionsException(String message) {
        super(message);
    }

    public OptionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
