package ru.yandex.practicum.task_tracker.exceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }
}