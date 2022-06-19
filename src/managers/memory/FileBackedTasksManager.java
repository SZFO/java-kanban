package managers.memory;

import exceptions.ManagerSaveException;
import managers.history.HistoryManager;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String BACKED_TASKS_PATH = "src\\textfiles\\tasks.csv";
    private static final String FILE_HEADER = "id,type,name,status,description,epic";
    private final File file;


    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(BACKED_TASKS_PATH));

        Epic epic2 = new Epic("Эпик №2", "Провести апгрейд сервера");
        fileBackedTasksManager.addEpic(epic2);
        fileBackedTasksManager.getEpic(epic2.getId());
        SubTask subtask3 = new SubTask("Подзадача №1", "Установить дополнительную оперативную память", TaskStatus.DONE, epic2.getId());
        fileBackedTasksManager.addSubTask(subtask3);
        fileBackedTasksManager.getSubTask(subtask3.getId());
    }


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {
        Map<Integer, String> tasks = new LinkedHashMap<>();
        for (Task task : getAllTasksList()) {
            tasks.put(task.getId(), taskToString(task));
        }
        for (Epic epic : getAllEpicsList()) {
            tasks.put(epic.getId(), epicToString(epic));
        }
        for (SubTask subTask : getAllSubTasksList()) {
            tasks.put(subTask.getId(), subTaskToString(subTask));
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(FILE_HEADER);
            bw.newLine();
            for (String task : tasks.values()) {
                bw.write(task);
                bw.newLine();
            }
            bw.newLine();
            bw.write(toString(getHistoryManager()));
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка сохранения в файл: " + file.getName());
        }
    }


    private static void addTask(FileBackedTasksManager manager, String str) {
        Task task = taskFromString(str);
        TaskType type = Objects.requireNonNull(task).getType();
        switch (type) {
            case TASK: {
                manager.addTask(task);
                break;
            }
            case EPIC: {
                manager.addEpic((Epic) task);
                break;
            }
            case SUBTASK: {
                manager.addSubTask((SubTask) task);
                break;
            }
        }
    }

    private static List<String> readCsv() {
        if (!Files.exists(Path.of(BACKED_TASKS_PATH)))
            return Collections.emptyList();
        List<String> bufferList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BACKED_TASKS_PATH))) {
            while (br.ready()) {
                bufferList.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferList;
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        List<String> tasks = FileBackedTasksManager.readCsv();
        FileBackedTasksManager fileBack = new FileBackedTasksManager(file);
        for (int i = 1; i < tasks.size() - 1; i++) {
            String str = tasks.get(i);
            if (str.isEmpty()) {
                tasks.remove(str);
                break;
            }
            addTask(fileBack, str);
        }

        if (!tasks.isEmpty()) {
            try {
                String[] temp = tasks.get(tasks.size() - 1).split(",");
                for (String s : temp) {
                    int element = Integer.parseInt(s);
                    fileBack.getHistoryManager().add(fileBack.getTaskUniversal(element));
                }
            } catch (NumberFormatException e) {
                System.out.println("История просмотров пуста");
            }
        }
        return fileBack;
    }


    private static Task taskFromString(String value) {
        String[] elements = value.split(",");
        for (int i = 0; i < elements.length; i++) {
            elements[i] = elements[i].trim();
        }
        switch (TaskType.valueOf(elements[1].toUpperCase())) {
            case TASK:
                return getTaskFromString(elements);
            case EPIC:
                return getEpicFromString(elements);
            case SUBTASK:
                return getSubTaskFromString(elements);
            default: {
                return null;
            }
        }
    }

    private static Task getTaskFromString(String[] elements) {
        return new Task(
                elements[2],
                elements[4],
                TaskStatus.valueOf(elements[3]));
    }

    private static Task getEpicFromString(String[] elements) {
        return new Epic(
                elements[2],
                elements[4]);
    }

    private static Task getSubTaskFromString(String[] elements) {
        return new SubTask(
                elements[2],
                elements[4],
                TaskStatus.valueOf(elements[3]),
                Integer.parseInt(elements[5]));
    }

    private static String toString(HistoryManager manager) {
        List<String> list = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            list.add(task.getId().toString());
        }
        return String.join(",", list);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public Task getTask(Integer id) {
        super.getTask(id);
        save();
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(Integer id) {
        super.getEpic(id);
        save();
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(Integer id) {
        super.getSubTask(id);
        save();
        return super.getSubTask(id);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(Integer id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }


    public String taskToString(Task task) {
        return task.getId() + "," +
                TaskType.TASK + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription();
    }

    public String epicToString(Epic epic) {
        return epic.getId() + "," +
                TaskType.EPIC + "," +
                epic.getName() + "," +
                epic.getStatus() + "," +
                epic.getDescription();
    }

    public String subTaskToString(SubTask subTask) {
        return subTask.getId() + "," +
                TaskType.SUBTASK + "," +
                subTask.getName() + "," +
                subTask.getStatus() + "," +
                subTask.getDescription() + "," +
                subTask.getEpicId();
    }

}








