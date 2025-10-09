package ru.itis.example.card_group.repositories.impl;

import ru.itis.example.card_group.repositories.CardGroupRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.CardGroup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardGroupRepositoryJdbcImpl implements CardGroupRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private final DataSource dataSource;

    public CardGroupRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<CardGroup> getCardGroups() {
        try (Connection connection = dataSource.getConnection()) {
            List<CardGroup> cardGroups = new ArrayList<>();

            String sql = "SELECT id, author_id, name FROM card_groups WHERE uploaded = TRUE";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CardGroup cardGroup = new CardGroup(
                        resultSet.getLong(1),
                        resultSet.getLong(2),
                        resultSet.getString(3),
                        true
                );
                cardGroups.add(cardGroup);
            }

            return cardGroups;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting card groups: " + e);
            throw new RuntimeException("database error occurred while getting card groups: " + e);
        }
    }
}
