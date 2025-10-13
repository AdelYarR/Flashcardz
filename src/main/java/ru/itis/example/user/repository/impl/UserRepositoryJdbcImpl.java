package ru.itis.example.user.repository.impl;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private final Logger logger = new Logger(this.getClass().getName());
    private final DataSource dataSource;

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long addUser(User user) {
        Long id = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO users (name, password) VALUES (?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.name());
            preparedStatement.setString(2, user.password());

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
            logger.error("Database error occurred while adding the user: " + e);
            throw new RuntimeException("database error occurred while adding the user: " + e);
        }
    }

    @Override
    public Optional<User> getUser(String name) {
        Optional<User> optionalUser;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, name, password FROM users WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
                optionalUser = Optional.of(user);
            } else {
                optionalUser = Optional.empty();
            }

            preparedStatement.close();
            resultSet.close();
            return optionalUser;
        } catch (SQLException e) {
            logger.error("Database error occurred while searching for the user " + name + ": " + e);
            throw new RuntimeException("database error occurred while searching for the user: " + e);
        }
    }
}
