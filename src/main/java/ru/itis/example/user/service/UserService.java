package ru.itis.example.user.service;

import ru.itis.example.logger.Logger;
import ru.itis.example.models.User;
import ru.itis.example.user.exceptions.UserAuthenticationException;
import ru.itis.example.user.exceptions.UserRegistrationException;
import ru.itis.example.user.exceptions.UserRepositoryException;
import ru.itis.example.user.exceptions.UserValidationException;
import ru.itis.example.user.repository.UserRepository;
import ru.itis.example.util.Hasher;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {

    private static final int MIN_NAME_LENGTH = 4;
    private static final int MAX_NAME_LENGTH = 32;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final String NAME_PATTERN = "^[a-zA-Z0-9]+$";

    private final Logger logger = new Logger(this.getClass().getName());
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long registryUser(String name, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            logger.info("User " + name + ": Passwords don't match.");
            throw new UserRegistrationException("passwords don't match");
        }
        if (getUser(name).isPresent()) {
            logger.info("User " + name + ": Name is already taken.");
            throw new UserRegistrationException("name is already taken");
        }

        validateName(name);
        validatePassword(password);
        logger.info("User " + name + ": Validation was successful.");

        String hashedPassword;
        try {
            hashedPassword = Hasher.hash(password);
        } catch (Exception e) {
            throw new UserRegistrationException("failed to hash the password: " + e);
        }
        logger.info("User " + name + ": Hashed password.");

        User user = new User(null, name, hashedPassword);

        Long userId = userRepository.addUser(user);
        logger.info("User " + name + " was successfully added.");
        return userId;
    }

    public Long logUser(String name, String password) {
        Optional<User> foundUser = getUser(name);
        if (foundUser.isEmpty()) {
            logger.info("User " + name + " is not found.");
            throw new UserAuthenticationException("user " + name + " is not found");
        }
        logger.info("User " + name + " is found.");

        User user = foundUser.get();
        String hashedPassword = user.password();
        try {
            if (Hasher.verify(password, hashedPassword)) {
                logger.info("User " + name + " was successfully logged.");
                return user.id();
            }
        } catch (Exception e) {
            logger.info("User " + name + ": Failed to unhash the password: " + e);
            throw new UserAuthenticationException("failed to unhash the password: " + e);
        }

        logger.info("User " + name + ": Passwords don't match.");
        throw new UserAuthenticationException("passwords don't match");
    }

    public Optional<User> getUser(String name) {
        return userRepository.getUser(name);
    }

    public void validateName(String name) {
        if (name.length() < MIN_NAME_LENGTH) {
            throw new UserValidationException("name is too short, the length must be at least 4");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new UserValidationException("name is too long, the length must be no more than 32");
        }

        if (!name.matches(NAME_PATTERN)) {
            throw new UserValidationException("name must contain only english letters and numbers");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH ) {
            throw new UserValidationException("password is too short, the length must be at least 8");
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new UserValidationException("password is too long, the length must be no more than 128");
        }
    }
}
