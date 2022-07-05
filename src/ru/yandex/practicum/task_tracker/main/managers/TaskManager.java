package ru.yandex.practicum.task_tracker.main.managers;

import ru.yandex.practicum.task_tracker.main.tasks.*;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTask(Integer id);

    SubTask getSubTask(Integer id);

    Epic getEpic(Integer id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    List<SubTask> getSubTasksInEpic(int epicId);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    void deleteAllTasks();

    List<Task> getHistory();

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    Set<Task> getPrioritizedTasks();

}