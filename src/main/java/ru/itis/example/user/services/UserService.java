package ru.itis.example.user.services;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.util.PasswordHasher;

import java.sql.SQLException;
import java.util.Optional;

public class UserService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registryUser(String name, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            logger.info("User " + name + ": Passwords don't match.");
            throw new IllegalArgumentException("passwords don't match");
        }
        if (getUser(name).isPresent()) {
            logger.info("User " + name + ": Name is already taken.");
            throw new IllegalArgumentException("name is already taken");
        }

        try {
            validateName(name);
            validatePassword(password);
        } catch (RuntimeException e) {
            logger.info("User " + name + ": " + e);
            throw new IllegalArgumentException(e);
        }

        logger.info("User " + name + ": Validation was successful.");

        String hashedPassword;
        try {
            hashedPassword = PasswordHasher.hashPassword(password);
        } catch (Exception e) {
            throw new RuntimeException("failed to hash the password: " + e);
        }
        logger.info("User " + name + ": Hashed password.");

        User user = new User(null, name, hashedPassword);

        try {
            userRepository.addUser(user);
            logger.info("User " + name + " was successfully added.");
            return user;
        } catch (SQLException e) {
            logger.error("Database error occurred while adding the user: " + e);
            throw new RuntimeException("database error occurred while adding the user: " + e);
        }
    }

    public User logUser(String name, String password) {
        Optional<User> foundUser = getUser(name);
        if (foundUser.isEmpty()) {
            logger.info("User " + name + " is not found.");
            throw new IllegalArgumentException("user " + name + " is not found");
        }
        logger.info("User " + name + " is found.");

        User user = foundUser.get();
        String hashedPassword = user.password();
        try {
            if (PasswordHasher.verifyPassword(password, hashedPassword)) {
                logger.info("User " + name + " was successfully logged.");
                return user;
            }
        } catch (Exception e) {
            logger.info("User " + name + ": Failed to unhash the password: " + e);
            throw new RuntimeException("failed to unhash the password: " + e);
        }

        logger.info("User " + name + ": Passwords don't match.");
        throw new IllegalArgumentException("passwords don't match");
    }

    public Optional<User> getUser(String name) {
        try {
            return userRepository.getUser(name);
        } catch (SQLException e) {
            logger.error("Database error occurred while searching for the user " + name + ": " + e);
            throw new RuntimeException("database error occurred while searching for the user: " + e);
        }
    }

    public void validateName(String name) {
        if (name.length() < 4) {
            throw new IllegalArgumentException("name is too short, the length must be at least 4");
        }

        if (name.length() > 32) {
            throw new IllegalArgumentException("name is too long, the length must be no more than 32");
        }

        if (!name.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("name must contain only english letters and numbers");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("password is too short, the length must be at least 8");
        }

        if (password.length() > 128) {
            throw new IllegalArgumentException("password is too long, the length must be no more than 128");
        }
    }
}
