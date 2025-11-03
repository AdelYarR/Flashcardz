package ru.itis.example.options.repository;

import ru.itis.example.models.UserCardSettings;

import java.util.Optional;

public interface OptionsRepository {

    Optional<UserCardSettings> getUserCardSettingsByUserId(Long userId);
    void update(UserCardSettings userCardSettings);
}
