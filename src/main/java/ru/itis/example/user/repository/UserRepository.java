package ru.itis.example.user.repository;

import ru.itis.example.models.User;

import java.util.Optional;

public interface UserRepository {
    Long addUser(User user);
    Optional<User> getUser(String name);
}
