package ru.yandex.practicum.tasktracker.managers;

import ru.yandex.practicum.tasktracker.history.HistoryManager;
import ru.yandex.practicum.tasktracker.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
