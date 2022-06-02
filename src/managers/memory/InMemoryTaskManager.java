package managers.memory;

import managers.Managers;
import managers.history.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

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
    public List<Task> getAllTasks() {
        return tasks.values().parallelStream().toList();
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return subTasks.values().parallelStream().toList();
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values().parallelStream().toList();
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
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpic().setSubTaskId(subTask.getId());
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
    public void updateSubTask(SubTask subTask) {
        for (SubTask elem : subTasks.values()) {
            if (subTask.getName().equals(elem.getName())) {
                subTask.setId(elem.getId());
                subTasks.put(subTask.getId(), subTask);
                subTask.getEpic().setStatus(calculateEpicStatus(subTask.getEpic()));
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic pastObjEpic = epics.get(epic.getId());
        List<Integer> pastSubTasks = pastObjEpic.getSubTasks();
        epic.setSubTasks(pastSubTasks);
        epics.put(epic.getId(), epic);
        for (Integer value : epic.getSubTasks()) {
            SubTask subTask = subTasks.get(value);
            subTask.setEpic(epic);
        }
        epic.setStatus(calculateEpicStatus(epic));
    }

    @Override
    public String subTasksInEpicToString(Epic epic) {
        if (epic.getSubTasks() == null) {
            return "У эпика отсутствуют подзадачи";
        } else {
            Map<Integer, SubTask> tempMap = new HashMap<>();
            for (Integer taskId : epic.getSubTasks()) {
                tempMap.put(taskId, subTasks.get(taskId));
            }
            StringBuilder stb = new StringBuilder();
            for (Map.Entry<Integer, SubTask> entry : tempMap.entrySet()) {
                stb.append(entry.getValue()).append("\n");
            }
            return stb.toString();
        }
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = subTask.getEpic();
        List<Integer> pastSubTasks = epic.getSubTasks();
        pastSubTasks.remove(id);
        subTasks.remove(id);
        historyManager.remove(id);
        epic.setStatus(calculateEpicStatus(epic));
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        List<Integer> pastSubTasks = epic.getSubTasks();
        for (Integer subTask : pastSubTasks) {
            subTasks.remove(subTask);
            historyManager.remove(subTask);
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

    private Integer generateId() {
        return id++;
    }

    private TaskStatus calculateEpicStatus(Epic epic) {
        int amountSubtask = 0;
        int amountStatusNew = 0;
        int amountStatusDone = 0;

        Map<Integer, SubTask> result = new HashMap<>();

        for (Integer id : epic.getSubTasks()) {
            result.put(id, subTasks.get(id));
            amountSubtask++;
        }

        for (SubTask subTask : result.values()) {
            if (subTask.getStatus().equals(TaskStatus.NEW)) {
                amountStatusNew++;
            } else if (subTask.getStatus().equals(TaskStatus.DONE)) {
                amountStatusDone++;
            }
        }

        if ((amountSubtask == amountStatusNew) || epic.getSubTasks().isEmpty()) {
            return TaskStatus.NEW;
        } else if (amountSubtask == amountStatusDone) {
            return TaskStatus.DONE;
        }
        return TaskStatus.IN_PROGRESS;
    }
}
