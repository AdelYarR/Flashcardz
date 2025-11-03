package ru.itis.example.card.services;

import ru.itis.example.card.exceptions.CardNotFoundException;
import ru.itis.example.card.repositories.CardRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;
import ru.itis.example.models.TrainingSession;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CardService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void add(Card card) {
        Long cardId = cardRepository.add(card);
        logger.info("Card " + cardId + " was successfully added.");
    }

    public List<Card> getCardsByGroupId(Long cardGroupId) {
        List<Card> cards = cardRepository.getCardsByGroupId(cardGroupId);
        logger.info("Cards were successfully taken from DB.");
        return cards;
    }

    public Card getCardById(Long cardId) {
        Optional<Card> optionalCard = cardRepository.getCardById(cardId);
        if (optionalCard.isEmpty()) {
            throw new CardNotFoundException("failed to get card by its id");
        }

        return optionalCard.get();
    }

    public void update(Card card) {
        cardRepository.update(card);
        logger.info("Card " + card.getId() + " was successfully updated.");
    }

    public void removeAllByGroupId(Long groupId) {
        cardRepository.removeAllByGroupId(groupId);
        logger.info("All cards with group id " + groupId + " were successfully removed from DB.");
    }

    public void removeByCardId(Long cardId) {
        cardRepository.removeByCardId(cardId);
        logger.info("Card " + cardId + " was successfully removed from DB.");
    }

    public void processCardUpdate(List<Card> cards, List<Card> newCards) {
        Map<Long, Card> cardsMap = cards.stream()
                .collect(Collectors.toMap(Card::getId, Function.identity()));

        for (Card card : newCards) {
            if (cardsMap.containsKey(card.getId())) {
                update(card);
            } else {
                add(card);
            }
            cardsMap.remove(card.getId());
        }
        cardsMap.forEach((key, val) -> removeByCardId(key));
    }

    public List<Card> buildCardsFromParams(Long authorId, Long cardGroupId, String[] cardIds, String[] questions, String[] answers) {
        List<Card> cards = new ArrayList<>();

        int max = Math.max(questions.length, cardIds.length);
        for (int i = 0; i < max; i++) {
            Long cardId = i < cardIds.length ? Long.valueOf(cardIds[i]) : null;
            cards.add(new Card(
                    cardId,
                    authorId,
                    cardGroupId,
                    questions[i],
                    answers[i]
            ));
        }
        return cards;
    }
}
