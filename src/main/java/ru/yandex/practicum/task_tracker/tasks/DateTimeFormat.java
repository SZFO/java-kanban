package ru.yandex.practicum.task_tracker.tasks;

import java.time.format.DateTimeFormatter;

public class DateTimeFormat {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    private DateTimeFormat() {
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }
}