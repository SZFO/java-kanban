package ru.yandex.practicum.task_tracker.main.managers;

import ru.yandex.practicum.task_tracker.main.exceptions.ManagerSaveException;
import ru.yandex.practicum.task_tracker.main.history.HistoryManager;
import ru.yandex.practicum.task_tracker.main.tasks.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class InMemoryTaskManager implements TaskManager {
    private Integer id;
    private Map<Integer, Task> tasks;
    private Map<Integer, SubTask> subTasks;
    private Map<Integer, Epic> epics;
    private HistoryManager historyManager;
    private Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.id = 1;
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    @Override
    public void addTask(Task task) {
        if (intersectionsDetected.test(task)) {
            throw new ManagerSaveException("При создании задачи обнаружено пересечение по времени выполнения.");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (intersectionsDetected.test(subTask)) {
            throw new ManagerSaveException("При создании подзадачи обнаружено пересечение по времени выполнения.");
        }
        int num = generateId();
        subTask.setEpicId(subTask.getEpicId());
        subTasks.put(num, subTask);
        subTask.setId(num);
        epics.get(subTask.getEpicId()).addSubTasks(subTask);
        epics.get(subTask.getEpicId()).calculateEpicStatus();
        prioritizedTasks.add(subTask);
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
        if (intersectionsDetected.test(task)) {
            throw new ManagerSaveException("При обновлении задачи обнаружено пересечение по времени выполнения.");
        }
        for (Task elem : tasks.values()) {
            if (task.getName().equals(elem.getName())) {
                prioritizedTasks.remove(elem);
                task.setId(elem.getId());
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        if (intersectionsDetected.test(newSubTask)) {
            throw new ManagerSaveException("При обновлении подзадачи обнаружено пересечение по времени выполнения.");
        }
        for (SubTask elem : subTasks.values()) {
            if (newSubTask.getName().equals(elem.getName())) {
                newSubTask.setId(elem.getId());
            }
        }
        SubTask oldSubTask = cloneSubTask(subTasks.get(newSubTask.getId()));
        Epic epic = epics.get(oldSubTask.getEpicId());
        prioritizedTasks.remove(oldSubTask);
        subTasks.remove(oldSubTask.getId());

        subTasks.put(newSubTask.getId(), newSubTask);
        prioritizedTasks.add(newSubTask);
        if (epic.getSubTasks().contains(oldSubTask)) {
            epic.getSubTasks().remove(oldSubTask);
            epic.getSubTasks().add(newSubTask);
        }
        epic.calculateEpicStatus();
    }

    @Override
    public void updateEpic(Epic epic) {
        for (Epic elem : epics.values()) {
            if (epic.getName().equals(elem.getName())) {
                epic.setId(elem.getId());
                epics.put(epic.getId(), epic);
                epic.calculateEpicStatus();
            }
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
        if (!tasks.containsKey(id)) {
            throw new ManagerSaveException("Задача с указанным Id отсутствует");
        }
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        if (!subTasks.containsKey(id)) {
            throw new ManagerSaveException("Подзадача с указанным Id отсутствует");
        }
        SubTask oldSubTask = cloneSubTask(subTasks.get(id));
        Epic epic = epics.get(oldSubTask.getEpicId());
        subTasks.remove(id);
        historyManager.remove(id);
        epic.getSubTasks().remove(oldSubTask);
        epic.calculateEpicStatus();
    }

    @Override
    public void deleteEpic(Integer id) {
        if (!epics.containsKey(id)) {
            throw new ManagerSaveException("Эпик с указанным Id отсутствует");
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
        prioritizedTasks.clear();
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public SubTask cloneSubTask(SubTask subTask) {
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                subTask.getEpicId(), subTask.getStartTime(), subTask.getDuration());
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

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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

    private final Predicate<Task> intersectionsDetected = newTask -> {
        if (newTask.getStartTime() == null) {
            return false;
        }
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskFinish = newTask.getEndTime();
        for (Task task : prioritizedTasks) {
            LocalDateTime taskStart = task.getStartTime();
            LocalDateTime taskFinish = task.getEndTime();
            if (taskStart == null) {
                continue;
            }
            if (newTaskStart.isBefore(taskStart) && newTaskFinish.isAfter(taskStart)) {
                return true;
            }
            if (newTaskStart.isBefore(taskFinish) && newTaskFinish.isAfter(taskFinish)) {
                return true;
            }
            if (newTaskStart.isEqual(taskStart) && newTaskFinish.isBefore(taskFinish)) {
                return true;
            }
        }
        return false;
    };
}