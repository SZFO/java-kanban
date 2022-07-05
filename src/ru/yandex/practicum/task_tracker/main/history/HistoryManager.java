package ru.yandex.practicum.task_tracker.main.history;

import ru.yandex.practicum.task_tracker.main.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}