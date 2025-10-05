package ru.itis.example.user.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itis.example.models.User;
import ru.itis.example.user.repositories.UserRepository;
import ru.itis.example.user.repositories.impl.UserRepositoryJdbcImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryJdbcImpl.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Провал регистрации из-за не совпадении паролей")
    void registryUser_PasswordsDontMatch() {
        String name = "username";
        String password = "password";
        String passwordConfirm = "different_password";

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал регистрации, имя уже занято другим пользователем")
    void registryUser_NameIsTaken() {
        String name = "username";
        String password = "password";
        String passwordConfirm = "password";

        User mockUser = new User(1L, "username", "test_password");
        Mockito.when(userService.getUser(name)).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал регистрации, длина имени меньше 4 символов")
    void registryUser_NameTooShort() {
        String name = "nam";
        String password = "password";
        String passwordConfirm = "password";

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал регистрации, длина имени больше 32 символов")
    void registryUser_NameTooLong() {
        String name = "A".repeat(33);
        String password = "password";
        String passwordConfirm = "password";

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }


    @Test
    @DisplayName("Провал регистрации, имя содержит символы кроме латиницы и цифр")
    void registryUser_NameHasInvalidSymbols() {
        String name = "usernameё";
        String password = "password";
        String passwordConfirm = "password";

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал регистрации, длина пароля меньше 8 символов")
    void registryUser_PasswordTooShort() {
        String name = "nam";
        String password = "pass";
        String passwordConfirm = "pass";

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал регистрации, длина пароля больше 128 символов")
    void registryUser_PasswordTooLong() {
        String name = "tooLongNameSpeciallyCreatedForTests";
        String password = "A".repeat(129);
        String passwordConfirm = "A".repeat(129);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registryUser(name, password, passwordConfirm));
    }

    @Test
    @DisplayName("Провал входа, пользователь не найден по имени")
    void logUser_NotFoundByName() {
        String name = "username";
        String password = "password";

        Optional<User> mockUser = Optional.empty();
        Mockito.when(userService.getUser("username")).thenReturn(mockUser);

        assertThrows(IllegalArgumentException.class,
                () -> userService.logUser(name, password));
    }

    @Test
    @DisplayName("Провал входа, введён неверный пароль")
    void logUser_PasswordsDontMatch() {
        String name = "username";
        String password = "different_password";

        User mockUser = new User(1L, "username", "password");

        assertThrows(IllegalArgumentException.class,
                () -> userService.logUser(name, password));
    }
}
