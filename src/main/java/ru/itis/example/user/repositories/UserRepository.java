package ru.itis.example.user.repositories;

import ru.itis.example.models.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    void addUser(User user) throws SQLException;
    Optional<User> getUser(String name) throws SQLException;
}
