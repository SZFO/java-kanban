package ru.yandex.practicum.task_tracker.main.java.managers;

import ru.yandex.practicum.task_tracker.main.java.history.HistoryManager;
import ru.yandex.practicum.task_tracker.main.java.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}