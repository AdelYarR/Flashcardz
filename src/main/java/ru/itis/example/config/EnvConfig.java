package ru.itis.example.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EnvConfig {
    public static final String ENV_PATH = "env.txt";
    public static final Map<String, String> ENV_MAP = new HashMap<>();

    static {
        try (InputStream inputStream = EnvConfig.class.getClassLoader().getResourceAsStream(ENV_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                ENV_MAP.put(parts[0], parts[1]);
            }
        } catch (IOException err) {
            throw new RuntimeException("failed to read the env file: " + err);
        }
    }

    public static String get(String key) {
        String value = ENV_MAP.get(key);
        if (value == null) {
            throw new RuntimeException("value is not found with the key: " + key);
        }

        return value;
    }
}
