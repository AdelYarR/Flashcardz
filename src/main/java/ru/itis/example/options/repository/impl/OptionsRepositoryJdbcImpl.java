package ru.itis.example.options.repository.impl;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.models.UserCardSettings;
import ru.itis.example.options.repository.OptionsRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class OptionsRepositoryJdbcImpl implements OptionsRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private DataSource dataSource;

    public OptionsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<UserCardSettings> getUserCardSettingsByUserId(Long userId) {
        Optional<UserCardSettings> optionalSettings;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, very_easy_seconds, easy_seconds, medium_seconds, hard_seconds FROM user_card_settings WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserCardSettings userCardSettings = new UserCardSettings(
                        resultSet.getLong("id"),
                        userId,
                        resultSet.getInt("very_easy_seconds"),
                        resultSet.getInt("easy_seconds"),
                        resultSet.getInt("medium_seconds"),
                        resultSet.getInt("hard_seconds")
                );
                optionalSettings = Optional.of(userCardSettings);
            } else {
                optionalSettings = Optional.empty();
            }

            preparedStatement.close();
            resultSet.close();
            return optionalSettings;
        } catch (SQLException e) {
            logger.error("Database error occurred while getting user card settings by user id: " + e);
            throw new RuntimeException("database error occurred while getting user card settings by user id: " + e);
        }
    }

    public void update(UserCardSettings userCardSettings) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE user_card_settings SET very_easy_seconds = ?, easy_seconds = ?, medium_seconds = ?, hard_seconds = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userCardSettings.getVeryEasySeconds());
            preparedStatement.setInt(2, userCardSettings.getEasySeconds());
            preparedStatement.setInt(3, userCardSettings.getMediumSeconds());
            preparedStatement.setInt(4, userCardSettings.getHardSeconds());
            preparedStatement.setLong(5, userCardSettings.getUserId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Database error occurred while updating user card settings: " + e);
            throw new RuntimeException("database error occurred while updating user card settings: " + e);
        }
    }
}
