package ru.yandex.practicum.task_tracker.exceptions;

public class HttpException extends RuntimeException {
    public HttpException(final String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }
}
