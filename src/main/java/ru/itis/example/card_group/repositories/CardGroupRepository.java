package ru.itis.example.card_group.repositories;

import ru.itis.example.models.CardGroup;

import java.sql.SQLException;
import java.util.List;

public interface CardGroupRepository {
    List<CardGroup> getCardGroups();
}
