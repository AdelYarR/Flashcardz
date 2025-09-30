package ru.itis.example.user.services;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.util.PasswordHasher;

import java.sql.SQLException;
import java.util.Optional;

public class UserRegistryService {

    private final Logger logger = new Logger(this.getClass().getName());
    private final UserRepository userRepository;

    public UserRegistryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(String name, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            logger.info("Passwords don't match.");
            throw new IllegalArgumentException("passwords don't match");
        }
        if (getUser(name).isPresent()) {
            logger.info("Name is already taken.");
            throw new IllegalArgumentException("name is already taken");
        }

        validateName(name);
        validatePassword(password);
        logger.info("Validation was successful.");

        String hashedPassword;
        try {
            hashedPassword = PasswordHasher.hashPassword(password);
        } catch (Exception err) {
            throw new RuntimeException("failed to hash the password: " + err);
        }
        logger.info("Hashed password.");
        
        User user = new User(null, name, hashedPassword);

        try {
            userRepository.addUser(user);
            logger.info("User " + name + " was successfully added.");
            return user;
        } catch (SQLException err) {
            logger.error("Database error occurred while adding the user: " + err);
            throw new RuntimeException("database error occurred while adding the user: " + err);
        }
    }

    public Optional<User> getUser(String name) {
        try {
            return userRepository.getUser(name);
        } catch (SQLException err) {
            logger.error("Database error occurred while searching for the user: " + err);
            throw new RuntimeException("database error occurred while searching for the user: " + err);
        }
    }

    public void validateName(String name) {
        if (name.length() < 4) {
            logger.info("Name length is under 4.");
            throw new IllegalArgumentException("name is too short, the length must be at least 4");
        }

        if (name.length() > 32) {
            logger.info("Name length is higher than 32.");
            throw new IllegalArgumentException("name is too long, the length must be no more than 32");
        }

        if (!name.matches("^[a-zA-Z0-9]+$")) {
            logger.info("Name includes forbidden symbols.");
            throw new IllegalArgumentException("name must contain only english letters and numbers");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < 8) {
            logger.info("Password length is under 8.");
            throw new IllegalArgumentException("password is too short, the length must be at least 8");
        }

        if (password.length() > 128) {
            logger.info("Name length is higher than 128.");
            throw new IllegalArgumentException("password is too long, the length must be no more than 128");
        }
    }
}
