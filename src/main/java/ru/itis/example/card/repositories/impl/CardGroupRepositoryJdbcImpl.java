package ru.itis.example.card.repositories.impl;

import ru.itis.example.card.repositories.CardGroupRepository;
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
    public Long add(Long authorId, String cardGroupName) {
        try (Connection connection = dataSource.getConnection()) {
            Long id = null;

            String sql = "INSERT INTO card_groups (author_id, name, uploaded) VALUES (?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, authorId);
            preparedStatement.setString(2, cardGroupName);
            preparedStatement.setBoolean(3, false);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }

            preparedStatement.close();
            resultSet.close();
            if (id == null) {
                throw new RuntimeException("failed to get generated ID");
            } else {
                return id;
            }
        } catch (SQLException e) {
            logger.error("Database error occurred while adding card group: " + e);
            throw new RuntimeException("database error occurred while adding card group: " + e);
        }
    }

    @Override
    public List<CardGroup> getCardGroups() {
        try (Connection connection = dataSource.getConnection()) {
            List<CardGroup> cardGroups = new ArrayList<>();

            String sql = """
                SELECT cg.id, u.name as author_name, cg.name, cg.uploaded
                FROM card_groups cg
                JOIN users u ON cg.author_id = u.id
                WHERE cg.uploaded = true
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CardGroup cardGroup = new CardGroup(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        true
                );
                cardGroups.add(cardGroup);
            }

            preparedStatement.close();
            return cardGroups;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting card groups: " + e);
            throw new RuntimeException("database error occurred while getting card groups: " + e);
        }
    }

    @Override
    public List<CardGroup> getCardGroupsByAuthorId(Long authorId) {
        try (Connection connection = dataSource.getConnection()) {
            List<CardGroup> cardGroups = new ArrayList<>();

            String sql = "SELECT id, name, uploaded FROM card_groups WHERE author_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, authorId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CardGroup cardGroup = new CardGroup(
                        resultSet.getLong(1),
                        null,
                        resultSet.getString(2),
                        resultSet.getBoolean(3)
                );
                System.out.println(cardGroup);
                cardGroups.add(cardGroup);
            }

            preparedStatement.close();
            return cardGroups;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting card groups by author id: " + e);
            throw new RuntimeException("database error occurred while getting card groups by author id: " + e);
        }
    }

    @Override
    public void removeByGroupId(Long cardGroupId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM card_groups WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardGroupId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while deleting card group by group id: " + e);
            throw new RuntimeException("database error occurred while delete card group by group id: " + e);
        }
    }

    @Override
    public void updateCardGroupName(Long cardGroupId, String newCardGroupName) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE card_groups SET name = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newCardGroupName);
            preparedStatement.setLong(2, cardGroupId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while updating card group name: " + e);
            throw new RuntimeException("database error occurred while updating card group name: " + e);
        }
    }

    @Override
    public void publishByGroupId(Long cardGroupId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE card_groups SET uploaded = true WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardGroupId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while publishing card group: " + e);
            throw new RuntimeException("database error occurred while publishing card group: " + e);
        }
    }
}
