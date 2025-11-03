package ru.itis.example.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
