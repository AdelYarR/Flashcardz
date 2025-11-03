package ru.itis.example.models;

import java.time.Duration;

public class DurationWrapper {

    private Duration duration;
    private String name;
    private String russianName;

    public DurationWrapper(Duration duration, String name, String russianName) {
        this.duration = duration;
        this.name = name;
        this.russianName = russianName;
    }

    public long getDays() { return duration.toDays(); }

    public int getHours() { return duration.toHoursPart(); }

    public int getMinutes() { return duration.toMinutesPart(); }

    public int getSeconds() { return duration.toSecondsPart(); }

    public String getName() { return this.name; }

    public String getRussianName() { return this.russianName; }
}
