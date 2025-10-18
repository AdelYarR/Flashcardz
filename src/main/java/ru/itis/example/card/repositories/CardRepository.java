package ru.itis.example.card.repositories;

import ru.itis.example.models.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    Long add(Card card);
    List<Card> getCardsByGroupId(Long cardGroupId);
    Optional<Card> getCardById(Long cardId);
    void update(Card card);
    void removeAllByGroupId(Long cardGroupId);
    void removeByCardId(Long cardId);
}
