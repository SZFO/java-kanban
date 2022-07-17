package ru.yandex.practicum.task_tracker.history;

import ru.yandex.practicum.task_tracker.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private OptimizedSprintFiveLinkedList<Task> taskViewsHistory;

    public InMemoryHistoryManager() {
        this.taskViewsHistory = new OptimizedSprintFiveLinkedList<>();
    }

    @Override
    public void add(Task task) {
        taskViewsHistory.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskViewsHistory.getTasks();
    }

    @Override
    public void remove(int id) {
        taskViewsHistory.remove(id);
    }
}