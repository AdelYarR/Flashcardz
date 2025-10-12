package ru.itis.example.auth.repository.impl;

import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.logger.Logger;
import ru.itis.example.models.Session;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SessionRepositoryJdbcImpl implements SessionRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private final DataSource dataSource;

    public SessionRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Session session) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO sessions (session_id, user_id, max_inactive, last_access) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, session.getSessionId());
            preparedStatement.setLong(2, session.getUserId());
            preparedStatement.setInt(3, session.getMaxInactive());
            preparedStatement.setLong(4, session.getLastAccess());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while adding the session " + session.getSessionId() + ": " + e);
            throw new RuntimeException("database error occurred while adding the session: " + e);
        }
    }

    @Override
    public void update(Session session) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE sessions SET session_id = ?, max_inactive = ?, last_access = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, session.getSessionId());
            preparedStatement.setInt(2, session.getMaxInactive());
            preparedStatement.setLong(3, session.getLastAccess());
            preparedStatement.setLong(4, session.getUserId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while updating the session " + session.getSessionId() + ": " + e);
            throw new RuntimeException("database error occurred while updating the session: " + e);
        }
    }

    @Override
    public Optional<Session> findBySessionId(String sessionId) {
        Optional<Session> optionalSession;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT user_id, max_inactive, last_access FROM sessions WHERE session_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sessionId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Session session = new Session(
                        sessionId,
                        resultSet.getLong("user_id"),
                        resultSet.getInt("max_inactive"),
                        resultSet.getLong("last_access")
                );
                optionalSession = Optional.of(session);
            } else {
                optionalSession = Optional.empty();
            }

            preparedStatement.close();
            resultSet.close();
            return optionalSession;
        } catch (SQLException e) {
            logger.error("Database error occurred while searching for the session by id " + sessionId + ": " + e);
            throw new RuntimeException("database error occurred while searching for the session: " + e);
        }
    }

    @Override
    public Optional<Session> findByUserId(Long userId) {
        Optional<Session> optionalSession;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT session_id, max_inactive, last_access FROM sessions WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Session session = new Session(
                        resultSet.getString("session_id"),
                        userId,
                        resultSet.getInt("max_inactive"),
                        resultSet.getLong("last_access")
                );
                optionalSession = Optional.of(session);
            } else {
                optionalSession = Optional.empty();
            }

            preparedStatement.close();
            resultSet.close();
            return optionalSession;
        } catch (SQLException e) {
            logger.error("Database error occurred while searching for the session by user id " + userId + ": " + e);
            throw new RuntimeException("database error occurred while searching for the session: " + e);
        }
    }
}
