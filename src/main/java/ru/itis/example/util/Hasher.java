package ru.itis.example.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Hasher {
    public static String hash(String value) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedValue = md.digest(value.getBytes());

        return Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hashedValue);
    }

    public static boolean verify(String value, String hashedValue) throws Exception {
        String[] parts = hashedValue.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedValueBytes = md.digest(value.getBytes());

        return parts[1].equals(Base64.getEncoder().encodeToString(hashedValueBytes));
    }
}
