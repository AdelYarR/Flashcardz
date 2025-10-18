package ru.itis.example.card.services;

import ru.itis.example.card.repositories.CardGroupRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;

import java.util.List;

public class GroupService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final CardGroupRepository cardGroupRepository;

    public GroupService(CardGroupRepository cardGroupRepository) {
        this.cardGroupRepository = cardGroupRepository;
    }

    public Long add(Long authorId, String cardGroupName) {
        return cardGroupRepository.add(authorId, cardGroupName);
    }

    public List<CardGroup> getCardGroupsByAuthorId(Long authorId) {
        List<CardGroup> cardGroups = cardGroupRepository.getCardGroupsByAuthorId(authorId);
        logger.info("Card groups were successfully taken from DB.");
        return cardGroups;
    }

    public List<CardGroup> getCardGroups() {
        List<CardGroup> cardGroups = cardGroupRepository.getCardGroups();
        logger.info("Card groups were successfully taken from DB.");
        return cardGroups;
    }

    public void removeByGroupId(Long cardGroupId) {
        cardGroupRepository.removeByGroupId(cardGroupId);
        logger.info("Card group " + cardGroupId + " was successfully removed from DB.");
    }

    public void updateCardGroupName(Long cardGroupId, String newCardGroupName) {
        cardGroupRepository.updateCardGroupName(cardGroupId, newCardGroupName);
        logger.info("Card group " + cardGroupId + " successfully changed name.");
    }

    public void publishByGroupId(Long cardGroupId) {
        cardGroupRepository.publishByGroupId(cardGroupId);
        logger.info("Card group " + cardGroupId + " successfully published.");
    }

    public List<CardGroup> processGroupsByInput(List<CardGroup> cardGroups, String inputText) {
        if (inputText != null) {
            return cardGroups.stream()
                    .filter(g -> g.name().toLowerCase().contains(inputText.toLowerCase()))
                    .toList();
        }

        return cardGroups;
    }
}
