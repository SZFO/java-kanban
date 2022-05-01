import java.util.*;

public class TaskManager {
    private Integer id;
    private Map<Integer, Task> taskMap;
    private Map<Integer, SubTask> subTaskMap;
    private Map<Integer, Epic> epicMap;


    public TaskManager() {
        this.id = 1;
        this.taskMap = new HashMap<>();
        this.subTaskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
    }


    Integer generateId() {  // Обновление уникального ID для задач
        return id++;
    }


    public void printAllTasks() {
        taskMap.entrySet().forEach(entry -> {
            System.out.println(entry.getValue());
        });
        epicMap.entrySet().forEach(entry -> {
            System.out.println(entry.getValue());
        });
        subTaskMap.entrySet().forEach(entry -> {
            System.out.println(entry.getValue());
        });
    }

    public void addTask(Task task) {
        task.setId(generateId());
        taskMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epicMap.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTaskMap.put(subTask.getId(), subTask);
        subTask.getEpic().setSubTaskId(subTask.getId());
    }

    public Task getTask(Integer id) {

        if (taskMap.containsKey(id)) {
            Map<Integer, Task> tempMap = new HashMap<>();
            tempMap.put(id, taskMap.get(id));
            return tempMap.put(id, taskMap.get(id));
        } else if (epicMap.containsKey(id)) {
            Map<Integer, Epic> tempMap = new HashMap<>();
            tempMap.put(id, epicMap.get(id));
            return tempMap.put(id, epicMap.get(id));
        } else if (subTaskMap.containsKey(id)) {
            Map<Integer, SubTask> tempMap = new HashMap<>();
            tempMap.put(id, subTaskMap.get(id));
            return tempMap.put(id, subTaskMap.get(id));
        }
        return null;
    }

    public void updateTask(Task task, Integer id, TaskStatus status) {
        task.setStatus(status);
        task.setId(id);
        taskMap.put(id, task);
    }

    public void updateSubTask(SubTask subTask, Integer id, TaskStatus status) {
        subTask.setStatus(status);
        subTask.setId(id);
        subTaskMap.put(id, subTask);
        subTask.getEpic().setStatus(calculateEpicStatus(subTask.getEpic()));
    }

    public void updateEpic(Epic epic) {
        Epic pastObjEpic = epicMap.get(epic.getId());
        List<Integer> subTasks = pastObjEpic.getSubTasks();
        epic.setSubTasks(subTasks);
        epicMap.put(epic.getId(), epic);
        for (Integer value : epic.getSubTasks()) {
            SubTask subTask = subTaskMap.get(value);
            subTask.setEpic(epic);
        }
        epic.setStatus(calculateEpicStatus(epic));
    }

    public String getSubTasksInEpic(Epic epic) {
        if (epic.getSubTasks() == null) {
            return "У эпика отсутствуют подзадачи";
        } else {
            Map<Integer, SubTask> tempMap = new HashMap<>();
            for (Integer taskId : epic.getSubTasks()) {
                tempMap.put(taskId, subTaskMap.get(taskId));
            }
            StringBuilder stb = new StringBuilder();
            for (Map.Entry<Integer, SubTask> entry : tempMap.entrySet()) {
                stb.append(entry.getValue()).append("\n");
            }
            return stb.toString();
        }
    }

    public void deleteTask(Integer id) {
        if (taskMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача с указанным номером отсутствует в списке трекера.");
            return;
        }
        taskMap.remove(id);
    }

    public void deleteSubTask(Integer id) {
        if (subTaskMap.isEmpty()) {
            System.out.println("Список подзадач трекера пуст.");
            return;
        }
        if (!subTaskMap.containsKey(id)) {
            System.out.println("Подзадача с указанным номером отсутствует в списке трекера.");
            return;
        }
        SubTask subTask = subTaskMap.get(id);
        Epic epic = subTask.getEpic();
        List<Integer> subTasks = epic.getSubTasks();
        subTasks.remove(id);
        subTaskMap.remove(id);
        epic.setStatus(calculateEpicStatus(epic));
    }


    public void deleteEpic(Integer id) {
        if (epicMap.isEmpty()) {
            System.out.println("Список эпиков трекера пуст.");
            return;
        }
        if (!epicMap.containsKey(id)) {
            System.out.println("Эпик с указанным номером отсутствует в списке трекера.");
            return;
        }
        Epic epic = epicMap.get(id);
        List<Integer> subTasks = epic.getSubTasks();
        for (Integer subTask : subTasks) {
            subTaskMap.remove(subTask);
        }
        epicMap.remove(id);
    }

    public void deleteAllTasks() {
        boolean allTasksClean = taskMap.isEmpty() && subTaskMap.isEmpty() && epicMap.isEmpty();
        if (!allTasksClean) {
            taskMap.clear();
            subTaskMap.clear();
            epicMap.clear();
            System.out.println("Список задач трекера очищен.");
        } else {
            System.out.println("Список задач трекера уже пуст.");
        }
    }

    TaskStatus calculateEpicStatus(Epic epic) {
        int amountSubtask = 0;
        int amountStatusNew = 0;
        int amountStatusDone = 0;

        Map<Integer, SubTask> result = new HashMap<>();

        for (Integer id : epic.getSubTasks()) {
            result.put(id, subTaskMap.get(id));
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
