package ru.itis.example.user.repositories;

import ru.itis.example.models.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Long addUser(User user);
    Optional<User> getUser(String name);
}
