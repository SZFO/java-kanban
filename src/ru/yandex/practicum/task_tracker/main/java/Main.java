package ru.yandex.practicum.task_tracker.main.java;

import ru.yandex.practicum.task_tracker.main.java.server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}