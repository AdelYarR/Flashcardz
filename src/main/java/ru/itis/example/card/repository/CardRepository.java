package ru.itis.example.card.repository;

import ru.itis.example.models.Card;

import java.util.List;

public interface CardRepository {
    Long add(Card card);
    List<Card> getCardsByGroupId(Long cardGroupId);
    void update(Card card);
    void removeAllByGroupId(Long cardGroupId);
    void removeByCardId(Long cardId);
}
