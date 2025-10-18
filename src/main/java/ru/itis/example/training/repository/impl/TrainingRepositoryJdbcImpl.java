package ru.itis.example.training.repository.impl;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.TrainingSession;
import ru.itis.example.models.UserCardProgress;
import ru.itis.example.models.UserCardProgressWithSeconds;
import ru.itis.example.training.repository.TrainingRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrainingRepositoryJdbcImpl implements TrainingRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private final DataSource dataSource;

    public TrainingRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteOldTrainingSession(Long userId, Long cardGroupId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM training_sessions WHERE user_id = ? AND card_group_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, cardGroupId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while deleting training session: " + e);
            throw new RuntimeException("database error occurred while deleting training session: " + e);
        }
    }

    public List<UserCardProgressWithSeconds> getProgressesByUserAndCardGroupId(Long userId, Long cardGroupId) {
        List<UserCardProgressWithSeconds> userCardProgressWithSeconds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = """
                SELECT
                    ucp.id,
                    ucp.user_id,
                    ucp.card_id,
                    CASE ucp.difficulty
                        WHEN 'VERY_EASY' THEN ucs.very_easy_seconds
                        WHEN 'EASY' THEN ucs.easy_seconds
                        WHEN 'MEDIUM' THEN ucs.medium_seconds
                        WHEN 'HARD' THEN ucs.hard_seconds
                    END as max_seconds,
                    ucp.last_access
                FROM user_card_progress ucp
                JOIN user_card_settings ucs ON ucp.user_id = ucs.user_id
                WHERE ucp.user_id = ?;
            """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserCardProgressWithSeconds progress = new UserCardProgressWithSeconds(
                        resultSet.getLong("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getLong("card_id"),
                        resultSet.getInt("max_seconds"),
                        resultSet.getLong("last_access")
                );
                userCardProgressWithSeconds.add(progress);
            }

            preparedStatement.close();
            resultSet.close();
            return userCardProgressWithSeconds;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting user card progresses: " + e);
            throw new RuntimeException("database error occurred while getting user card progresses: " + e);
        }
    }

    public void addTrainingSession(TrainingSession trainingSession) {
        try (Connection connection = dataSource.getConnection()) {

            String sql = "INSERT INTO training_sessions (session_id, user_id, card_group_id, current_index, max_cards, max_inactive, last_access) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, trainingSession.getSessionId());
            preparedStatement.setLong(2, trainingSession.getUserId());
            preparedStatement.setLong(3, trainingSession.getCardGroupId());
            preparedStatement.setInt(4, trainingSession.getCurrentIndex());
            preparedStatement.setInt(5, trainingSession.getMaxCards());
            preparedStatement.setInt(6, trainingSession.getMaxInactive());
            preparedStatement.setLong(7, trainingSession.getLastAccess());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Database error occurred while adding training session: " + e);
            throw new RuntimeException("database error occurred while adding training session: " + e);
        }
    }

    public void addTrainingSessionCard(String trainingSessionId, Long trainingCardId, Integer cardOrder) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO training_session_cards (session_id, card_id, card_order) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, trainingSessionId);
            preparedStatement.setLong(2, trainingCardId);
            preparedStatement.setInt(3, cardOrder);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while adding training session card: " + e);
            throw new RuntimeException("database error occurred while adding training session card: " + e);
        }
    }

    public Optional<TrainingSession> getByTrainingSessionId(String trainingSessionId) {
        Optional<TrainingSession> optionalTrainingSession;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT user_id, card_group_id, current_index, max_cards, max_inactive, last_access FROM training_sessions WHERE session_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, trainingSessionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                TrainingSession trainingSession = new TrainingSession(
                        trainingSessionId,
                        resultSet.getLong("user_id"),
                        resultSet.getLong("card_group_id"),
                        resultSet.getInt("current_index"),
                        resultSet.getInt("max_cards"),
                        resultSet.getInt("max_inactive"),
                        resultSet.getLong("last_access")
                );
                optionalTrainingSession = Optional.of(trainingSession);
            } else {
                optionalTrainingSession = Optional.empty();
            }

            preparedStatement.close();
            resultSet.close();
            return optionalTrainingSession;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting training session: " + e);
            throw new RuntimeException("database error occurred while getting training session: " + e);
        }
    }

    public List<Long> getTrainingCardIdsByTrainingSessionId(String trainingSessionId) {
        List<Long> trainingCardIds = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT card_id FROM training_session_cards WHERE session_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, trainingSessionId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                trainingCardIds.add(resultSet.getLong("card_id"));
            }

            preparedStatement.close();
            resultSet.close();
            return trainingCardIds;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting training card ids by training session id: " + e);
            throw new RuntimeException("database error occurred while getting training card ids by training session id:" + e);
        }
    }

    public boolean existsUserCardProgressByUserAndCardId(Long userId, Long cardId) {
        boolean exists = false;

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT EXISTS(SELECT 1 FROM user_card_progress WHERE user_id = ? AND card_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, cardId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exists = resultSet.getBoolean(1);
            }

            preparedStatement.close();
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            logger.error("Database error occurred while checking existence of user card progress: " + e);
            throw new RuntimeException("database error occurred while checking existence of user card progress: " + e);
        }
    }

    public void addUserCardProgress(UserCardProgress userCardProgress) {
        try (Connection connection = dataSource.getConnection()) {

            String sql = "INSERT INTO user_card_progress (user_id, card_id, difficulty, last_access) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, userCardProgress.getUserId());
            preparedStatement.setLong(2, userCardProgress.getCardId());
            preparedStatement.setString(3, userCardProgress.getDifficulty().name());
            preparedStatement.setLong(4, userCardProgress.getLastAccess());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Database error occurred while adding new user card progress: " + e);
            throw new RuntimeException("database error occurred while adding new user card progress: " + e);
        }
    }

    public void updateUserCardProgress(UserCardProgress userCardProgress) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE user_card_progress SET difficulty = ?, last_access = ? WHERE user_id = ? AND card_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, userCardProgress.getDifficulty().name());
            preparedStatement.setLong(2, userCardProgress.getLastAccess());
            preparedStatement.setLong(3, userCardProgress.getUserId());
            preparedStatement.setLong(4, userCardProgress.getCardId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while updating user card progress: " + e);
            throw new RuntimeException("database error occurred while updating user card progress: " + e);
        }
    }

    public void updateIndexOfTrainingSession(Integer index, String trainingSessionId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE training_sessions SET current_index = ? WHERE session_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, index);
            preparedStatement.setString(2, trainingSessionId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while incrementing current index of training session: " + e);
            throw new RuntimeException("database error occurred while incrementing current index of training session: " + e);
        }
    }
}
