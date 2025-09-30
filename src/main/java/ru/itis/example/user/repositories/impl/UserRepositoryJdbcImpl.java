package ru.itis.example.user.repositories.impl;

import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private Connection connection;

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.name());
        preparedStatement.setString(2, user.password());

        preparedStatement.executeUpdate();
    }

    @Override
    public Optional<User> getUser(String name) throws SQLException {
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
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
