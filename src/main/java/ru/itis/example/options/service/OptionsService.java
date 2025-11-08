package ru.itis.example.options.service;

import ru.itis.example.models.User;
import ru.itis.example.models.UserCardSettings;
import ru.itis.example.options.exceptions.OptionsNotFoundException;
import ru.itis.example.options.repository.OptionsRepository;

import java.util.Optional;

public class OptionsService {

    private final OptionsRepository optionsRepository;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public void add(UserCardSettings userCardSettings) {
        optionsRepository.add(userCardSettings);
    }

    public UserCardSettings getUserCardSettingsByUserId(Long userId) {
        Optional<UserCardSettings> optionalSettings = optionsRepository.getUserCardSettingsByUserId(userId);
        if (optionalSettings.isEmpty()) {
            throw new OptionsNotFoundException("failed to get user card settings by user id");
        }

        return optionalSettings.get();
    }

    public void update(UserCardSettings userCardSettings) {
        optionsRepository.update(userCardSettings);
    }
}
