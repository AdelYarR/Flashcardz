package ru.itis.example.card.exceptions;

public class CardNotFoundException extends CardException {
    public CardNotFoundException(String message) {
        super(message);
    }
}
