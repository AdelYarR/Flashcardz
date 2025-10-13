package ru.itis.example.util;

import jakarta.servlet.http.Cookie;

import java.util.Arrays;

public class CookieHelper {
    public static String getValueFromCookies(Cookie[] cookies, String key) {
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> key.equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
