import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Integer taskId;
    private Map<Integer, Task> allTasksMap;

    public TaskManager() {
        taskId = 1;
        allTasksMap = new HashMap<>();
    }

    public Integer newTaskId() {  // Обновление уникального ID для задач
        return taskId++;
    }

    public void printListAllTasks() {
        if (allTasksMap.isEmpty()) {
            System.out.println("В трекере задач отсутствуют введённые задачи");
            return;
        }
        for (Integer key : allTasksMap.keySet()) {
            Task task = allTasksMap.get(key);
            System.out.println(task);
        }
    }

    public void addTask(Task task) {
        if (task.getTaskId() == null) {
            task.setTaskId(newTaskId());
            allTasksMap.put(task.getTaskId(), task);
        } else {
            System.out.println("При добавлении задачи в трекер произошла ошибка.");
        }
    }

    public void addEpic(Epic epic) {
        if (epic.getTaskId() == null) {
            epic.setTaskId(newTaskId());
            allTasksMap.put(epic.getTaskId(), epic);
        } else {
            System.out.println("При добавлении эпика в трекер произошла ошибка.");
        }
    }

    public void addSubTask(Integer epicId, SubTask subTask) {
        if (allTasksMap.containsKey(epicId)) {
            subTask.setTaskId(newTaskId());
            subTask.setEpicId(epicId);
            allTasksMap.put(subTask.getTaskId(), subTask);
            Epic epic = (Epic) allTasksMap.get(epicId);
            List<Integer> subTasks = epic.getSubTasks();
            subTasks.add(subTask.getTaskId());
        } else {
            System.out.println("Эпик с данным номером отсутствует в трекере задач.");
        }
    }


    public void delAllTasks() {
        if (!allTasksMap.isEmpty()) {
            allTasksMap.clear();
            System.out.println("Список задач трекера очищен.");
        } else {
            System.out.println("Список задач трекера уже пуст.");
        }
    }

    public void getTask(Integer taskId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(taskId)) {
            System.out.println("Задача с указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(taskId);
        if (!(task instanceof SubTask) && !(task instanceof Epic)) {
            System.out.println(task);
        } else {
            System.out.println("Введённый номер не соответствует ни одной задаче.");
        }
    }

    public void getEpic(Integer epicId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(epicId)) {
            System.out.println("Эпик с указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(epicId);
        if (task instanceof Epic) {
            System.out.println(task);
        } else {
            System.out.println("Введённый номер не соответствует ни одному эпику.");
        }
    }

    public void getSubTask(Integer subTaskId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(subTaskId)) {
            System.out.println("Подзадача указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(subTaskId);
        if (task instanceof SubTask) {
            System.out.println(task);
        } else {
            System.out.println("Введённый номер не соответствует ни одной подзадаче.");
        }
    }

    public void delTask(Integer taskId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(taskId)) {
            System.out.println("Задача с указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(taskId);
        if (!(task instanceof SubTask) && !(task instanceof Epic)) {
            allTasksMap.remove(taskId);
        } else {
            System.out.println("Введённый номер не соответствует ни одной задаче.");
        }
    }

    public void delSubTask(Integer subTaskId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(subTaskId)) {
            System.out.println("Подзадача указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(subTaskId);
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) allTasksMap.get(subTaskId);
            Epic epic = (Epic) allTasksMap.get(subTask.getEpicId());
            List<Integer> subTasks = epic.getSubTasks();
            subTasks.remove(subTaskId);
            allTasksMap.remove(subTaskId);
            epic.setTaskStatus(getNewEpicStatus(epic));
        } else {
            System.out.println("Введённый номер не соответствует ни одной подзадаче.");
        }
    }

    public void delEpic(Integer epicId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (!allTasksMap.containsKey(epicId)) {
            System.out.println("Эпик с указанным номером отсутствует в списке задач трекера.");
            return;
        }
        Task task = allTasksMap.get(epicId);
        if (task instanceof Epic) {
            Epic epic = (Epic) allTasksMap.get(epicId);
            List<Integer> subTasks = epic.getSubTasks();
            for (Integer subTask : subTasks) {
                allTasksMap.remove(subTask);
            }
            allTasksMap.remove(epicId);
        } else {
            System.out.println("Введённый номер не соответствует ни одному эпику.");
        }
    }

    public void updTask(Task task) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (allTasksMap.containsKey(task.getTaskId())) {
            allTasksMap.put(task.getTaskId(), task);
        } else {
            System.out.println("При обновлении задачи в трекере произошла ошибка.");
        }
    }

    public void updSubTask(SubTask subTask) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (allTasksMap.containsKey(subTask.getTaskId())) {
            SubTask pastObjSubTask = (SubTask) allTasksMap.get(subTask.getTaskId());
            subTask.setEpicId(pastObjSubTask.getEpicId());
            allTasksMap.put(subTask.getTaskId(), subTask);
            Epic epic = (Epic) allTasksMap.get(subTask.getEpicId());
            epic.setTaskStatus(getNewEpicStatus(epic));
        } else {
            System.out.println("Подзадача отсутствует в списке задач трекера.");
        }
    }

    public void updEpic(Epic epic) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (allTasksMap.containsKey(epic.getTaskId())) {
            Epic pastObjEpic = (Epic) allTasksMap.get(epic.getTaskId());
            List<Integer> subTasks = pastObjEpic.getSubTasks();
            epic.setSubTasks(subTasks);
            allTasksMap.put(epic.getTaskId(), epic);
            epic.setTaskStatus(getNewEpicStatus(epic));
        } else {
            System.out.println("Эпик отсутствует в списке задач трекера.");
        }
    }

    public void printListSubTaskInEpic(Integer epicId) {
        if (allTasksMap.isEmpty()) {
            System.out.println("Список задач трекера пуст.");
            return;
        }
        if (allTasksMap.containsKey(epicId)) {
            Epic epic = (Epic) allTasksMap.get(epicId);
            List<Integer> subTasks = epic.getSubTasks();
            if (!subTasks.isEmpty()) {
                for (Integer subTaskId : subTasks) {
                    SubTask subTask = (SubTask) allTasksMap.get(subTaskId);
                    System.out.println(subTask);
                }
            } else {
                System.out.println("У эпика отсутствуют подзадачи");
            }
        } else {
            System.out.println("Введённый номер не соответствует ни одному эпику.");
        }
    }

    public TaskStatus getNewEpicStatus(Epic epic) {
        int amountSubtask = 0;
        int amountStatusNew = 0;
        int amountStatusDone = 0;

        Map<Integer, SubTask> result = new HashMap<>();

        for (Integer taskId : epic.getSubTasks()) {
            result.put(taskId, (SubTask) allTasksMap.get(taskId));
            amountSubtask++;
        }

        for (SubTask subTask : result.values()) {
            if (subTask.getTaskStatus().equals(TaskStatus.NEW)) {
                amountStatusNew++;
            } else if (subTask.getTaskStatus().equals(TaskStatus.DONE)) {
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
