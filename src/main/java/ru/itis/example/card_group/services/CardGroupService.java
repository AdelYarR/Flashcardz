package ru.itis.example.card_group.services;

import ru.itis.example.card_group.repositories.CardGroupRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;

import java.sql.SQLException;
import java.util.List;

public class CardGroupService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final CardGroupRepository cardGroupRepository;

    public CardGroupService(CardGroupRepository cardGroupRepository) {
        this.cardGroupRepository = cardGroupRepository;
    }

    public List<CardGroup> getCardGroups() {
        List<CardGroup> cardGroups = cardGroupRepository.getCardGroups();
        logger.info("Card groups were successfully taken from DB.");
        return cardGroups;
    }
}
