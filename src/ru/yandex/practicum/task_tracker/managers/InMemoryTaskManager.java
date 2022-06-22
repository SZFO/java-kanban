package ru.yandex.practicum.task_tracker.managers;

import ru.yandex.practicum.task_tracker.history.HistoryManager;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;
import ru.yandex.practicum.task_tracker.tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer id;
    private Map<Integer, Task> tasks;
    private Map<Integer, SubTask> subTasks;
    private Map<Integer, Epic> epics;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.id = 1;
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int num = generateId();
        subTask.setEpicId(subTask.getEpicId());
        subTasks.put(num, subTask);
        subTask.setId(num);
        epics.get(subTask.getEpicId()).addSubTasks(subTask);
        epics.get(subTask.getEpicId()).calculateEpicStatus();
    }

    @Override
    public Task getTask(Integer id) {
        Map<Integer, Task> tempMap = new HashMap<>();
        historyManager.add(tasks.get(id));
        tempMap.put(id, tasks.get(id));
        return tempMap.put(id, tasks.get(id));
    }

    @Override
    public SubTask getSubTask(Integer id) {
        Map<Integer, SubTask> tempMap = new HashMap<>();
        historyManager.add(subTasks.get(id));
        tempMap.put(id, subTasks.get(id));
        return tempMap.put(id, subTasks.get(id));
    }

    @Override
    public Epic getEpic(Integer id) {
        Map<Integer, Epic> tempMap = new HashMap<>();
        historyManager.add(epics.get(id));
        tempMap.put(id, epics.get(id));
        return tempMap.put(id, epics.get(id));
    }

    @Override
    public void updateTask(Task task) {
        for (Task elem : tasks.values()) {
            if (task.getName().equals(elem.getName())) {
                task.setId(elem.getId());
                tasks.put(task.getId(), task);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        for (SubTask elem : subTasks.values()) {
            if (newSubTask.getName().equals(elem.getName())) {
                newSubTask.setId(elem.getId());
            }
        }
        SubTask oldSubTask = cloneSubTask(subTasks.get(newSubTask.getId()));
        Epic epic = epics.get(oldSubTask.getEpicId());
        subTasks.remove(oldSubTask.getId());

        subTasks.put(newSubTask.getId(), newSubTask);
        if (epic.getSubTasks().contains(oldSubTask)) {
            epic.getSubTasks().remove(oldSubTask);
            epic.getSubTasks().add(newSubTask);
        }
        epic.calculateEpicStatus();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            epic.calculateEpicStatus();
        }
    }

    @Override
    public List<SubTask> getSubTasksInEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return Collections.emptyList();
        }
        Epic epic = epics.get(epicId);
        return epic.getSubTasks();
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        if (!subTasks.containsKey(id)) {
            return;
        }
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        if (!epics.containsKey(id)) {
            return;
        }
        for (SubTask subtask : epics.get(id).getSubTasks()) {
            subTasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public SubTask cloneSubTask(SubTask subTask) {
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(), subTask.getEpicId());
        newSubTask.setId(subTask.getId());
        newSubTask.setStatus(subTask.getStatus());
        return newSubTask;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Task getTaskUniversal(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else return subTasks.getOrDefault(id, null);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private Integer generateId() {
        return id++;
    }
}