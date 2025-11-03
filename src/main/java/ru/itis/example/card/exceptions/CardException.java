package ru.itis.example.card.exceptions;

public class CardException extends RuntimeException {
    public CardException(String message) {
        super(message);
    }

  public CardException(String message, Throwable cause) {
    super(message, cause);
  }
}
