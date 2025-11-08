package ru.itis.example.card.repositories.impl;

import ru.itis.example.card.exceptions.CardRepositoryException;
import ru.itis.example.card.repositories.CardRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Card;
import ru.itis.example.models.TrainingSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepositoryJdbcImpl implements CardRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private final DataSource dataSource;

    public CardRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long add(Card card) {
        try (Connection connection = dataSource.getConnection()) {
            Long id = null;

            String sql =  "INSERT INTO cards (author_id, card_group_id, question, answer) VALUES (?, ?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, card.getAuthorId());
            preparedStatement.setLong(2, card.getCardGroupId());
            preparedStatement.setString(3, card.getQuestion());
            preparedStatement.setString(4, card.getAnswer());

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
            logger.error("Database error occurred while adding card: " + e);
            throw new CardRepositoryException("database error occurred while adding card: " + e);
        }
    }

    @Override
    public List<Card> getCardsByGroupId(Long cardGroupId) {
        try (Connection connection = dataSource.getConnection()) {
            List<Card> cards = new ArrayList<>();

            String sql = "SELECT id, author_id, question, answer FROM cards WHERE card_group_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardGroupId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Card card = new Card(
                        resultSet.getLong("id"),
                        resultSet.getLong("author_id"),
                        cardGroupId,
                        resultSet.getString("question"),
                        resultSet.getString("answer")
                );
                cards.add(card);
            }

            preparedStatement.close();
            resultSet.close();
            return cards;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting cards: " + e);
            throw new CardRepositoryException("database error occurred while getting cards: " + e);
        }
    }

    public Optional<Card> getCardById(Long cardId) {
        Optional<Card> optionalCard;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT author_id, card_group_id, question, answer FROM cards WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Card card = new Card(
                        cardId,
                        resultSet.getLong("author_id"),
                        resultSet.getLong("card_group_id"),
                        resultSet.getString("question"),
                        resultSet.getString("answer")
                );
                optionalCard = Optional.of(card);
            } else {
                optionalCard = Optional.empty();
            }

            preparedStatement.close();
            return optionalCard;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting card by its id: " + e);
            throw new CardRepositoryException("database error occurred while getting card by its id: " + e);
        }
    }

    @Override
    public void update(Card card) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE cards SET question = ?, answer = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, card.getQuestion());
            preparedStatement.setString(2, card.getAnswer());
            preparedStatement.setLong(3, card.getId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while updating card: " + e);
            throw new CardRepositoryException("database error occurred while updating card: " + e);
        }
    }

    @Override
    public void removeAllByGroupId(Long cardGroupId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM cards WHERE card_group_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardGroupId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while deleting cards by card group id: " + e);
            throw new CardRepositoryException("database error occurred while deleting cards by card group id: " + e);
        }
    }

    @Override
    public void removeByCardId(Long cardId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM cards WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, cardId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while deleting card by id: " + e);
            throw new CardRepositoryException("database error occurred while deleting card by id: " + e);
        }
    }
}
