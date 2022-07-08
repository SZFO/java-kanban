package ru.yandex.practicum.task_tracker.main.java.exceptions;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(final String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }
}
