package ru.itis.example.models;

import org.jetbrains.annotations.Nullable;

public record User(@Nullable Long id, String name, String password) {
}
