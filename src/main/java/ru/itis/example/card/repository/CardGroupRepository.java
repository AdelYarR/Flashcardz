package ru.itis.example.card.repository;

import ru.itis.example.models.CardGroup;

import java.util.List;

public interface CardGroupRepository {
    Long add(Long authorId, String cardGroupName);
    List<CardGroup> getCardGroups();
    List<CardGroup> getCardGroupsByAuthorId(Long authorId);
    void removeByGroupId(Long groupId);
    void updateCardGroupName(Long cardGroupId, String newCardGroupName);
}
