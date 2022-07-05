package ru.yandex.practicum.task_tracker.main.managers;

import ru.yandex.practicum.task_tracker.main.history.HistoryManager;
import ru.yandex.practicum.task_tracker.main.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}