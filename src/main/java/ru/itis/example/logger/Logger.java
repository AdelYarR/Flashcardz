package ru.itis.example.logger;

public class Logger {

    private final String className;

    public Logger(String className) {
        this.className = className;
    }

    public void info(String message) {
        System.out.printf("[INFO][%s]: %s\n", className, message);
    }

    public void error(String message) {
        System.out.printf("[ERROR][%s]: %s\n", className, message);
    }
}
