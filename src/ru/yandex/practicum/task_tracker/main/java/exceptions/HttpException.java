package ru.yandex.practicum.task_tracker.main.java.exceptions;

public class HttpException extends RuntimeException {
    public HttpException(final String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }
}
