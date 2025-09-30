package ru.itis.example.util;

import java.io.*;

public class TxtEnvParser {

    private static final String ENV_PATH = "env.txt";

    public static String getEnv(String key) {
        String value = "";
        try (InputStream inputStream = TxtEnvParser.class.getClassLoader().getResourceAsStream(ENV_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts[0].equals(key)) {
                    value = parts[1];
                    break;
                }
            }

            if (value.isBlank()) {
                throw new RuntimeException("value is not found by key");
            }

            return value;
        } catch (IOException err) {
            throw new RuntimeException("failed to read the env file: " + err);
        }
    }
}
