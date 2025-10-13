package ru.itis.example.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private final String className;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(String className) {
        this.className = className;
    }

    public void info(String message) {
//        String time = LocalDateTime.now().format(formatter);
//        System.out.printf("[%s][INFO][%s]: %s\n", time, className, message);

        System.out.printf("[INFO][%s]: %s\n", className, message);
    }

    public void error(String message) {
//        String time = LocalDateTime.now().format(formatter);
//        System.out.printf("[%s][ERROR][%s]: %s\n", time, className, message);

        System.out.printf("[ERROR][%s]: %s\n", className, message);
    }
}
