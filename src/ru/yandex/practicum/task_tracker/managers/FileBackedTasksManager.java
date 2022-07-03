package ru.yandex.practicum.task_tracker.managers;

import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.*;

import ru.yandex.practicum.task_tracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.task_tracker.history.HistoryManager;
import ru.yandex.practicum.task_tracker.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String BACKED_TASKS_PATH = "resources/text-files/tasks.csv";
    private static final String FILE_HEADER = "id,type,name,status,description,epic,startTime,duration,endTime";
    private final File file;

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedManager = FileBackedTasksManager.loadFromFile(new File(BACKED_TASKS_PATH));

        Epic epic2 = new Epic("epic2", "This is Epic 2");
        fileBackedManager.addEpic(epic2);
        fileBackedManager.getEpic(epic2.getId());

        LocalDateTime time4 = LocalDateTime.of(2022, 7, 2, 12, 0, 0);
        Duration duration4 = Duration.ofMinutes(36);
        SubTask subtask3 = new SubTask("subTask3", "This is SubTask 3",
                DONE, epic2.getId(), time4, duration4);
        fileBackedManager.addSubTask(subtask3);
        fileBackedManager.getSubTask(subtask3.getId());
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        Map<Integer, String> tasks = new HashMap<>();
        for (Task task : getAllTasks()) {
            tasks.put(task.getId(), task.toString());
        }
        for (Epic epic : getAllEpics()) {
            tasks.put(epic.getId(), epic.toString());
        }
        for (SubTask subTask : getAllSubTasks()) {
            tasks.put(subTask.getId(), subTask.toString());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write(FILE_HEADER);
            bw.newLine();
            for (String task : tasks.values()) {
                bw.write(task);
                bw.newLine();
            }
            bw.newLine();
            bw.write(toString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл." + file.getName());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        List<String> tasks = FileBackedTasksManager.readCsv();
        FileBackedTasksManager fileBack = new FileBackedTasksManager(file);
        if (tasks.isEmpty()) {
            System.out.println("Файл пуст.");
            return fileBack;
        }
        for (int i = 1; i <= tasks.size() - 1; i++) {
            String str = tasks.get(i);
            if (str.isEmpty()) {
                tasks.remove(str);
                break;
            }
            addTask(fileBack, str);
        }
        String lastElement = tasks.get(tasks.size() - 1);
        // Проверка на наличие истории просмотров в файле.
        if (lastElement.length() > (tasks.size() - 1) * 2) {
            System.out.println("История прошлых просмотров пуста. Задачи считаны.");
            return fileBack;
        } else {
            String[] temp = lastElement.split(",");
            for (int counter = temp.length - 1; counter >= 0; counter--) {
                int element = Integer.parseInt(temp[counter]);
                fileBack.getHistoryManager().add(fileBack.getTaskUniversal(element));
            }
            return fileBack;
        }
    }

    private static void addTask(FileBackedTasksManager manager, String str) {
        Task task = taskFromString(str);
        TaskType type = Objects.requireNonNull(task).getType();
        switch (type) {
            case TASK -> manager.addTask(task);

            case EPIC -> manager.addEpic((Epic) task);

            case SUBTASK -> manager.addSubTask((SubTask) task);
        }
    }

    private static List<String> readCsv() {
        if (!Files.exists(Path.of(BACKED_TASKS_PATH))) {
            return Collections.emptyList();
        }
        List<String> bufferList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BACKED_TASKS_PATH, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                bufferList.add(br.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }
        return bufferList;
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
        if (elements[5].equals("Not set")) {
            return new Task(
                    Integer.parseInt(elements[0]),
                    elements[2],
                    elements[4],
                    valueOf(elements[3]),
                    null,
                    null);
        }
        return new Task(
                Integer.parseInt(elements[0]),
                elements[2],
                elements[4],
                valueOf(elements[3]),
                LocalDateTime.parse(elements[5], Task.dateTimeFormatter),
                Duration.parse(elements[6]));
    }

    private static Task getEpicFromString(String[] elements) {
        return new Epic(
                Integer.parseInt(elements[0]),
                elements[2],
                elements[4]);
    }

    private static Task getSubTaskFromString(String[] elements) {
        if (elements[5].equals("Not set")) {
            return new SubTask(
                    Integer.parseInt(elements[0]),
                    elements[2],
                    elements[4],
                    valueOf(elements[3]),
                    Integer.parseInt(elements[5]),
                    null,
                    null);
        }
        return new SubTask(
                Integer.parseInt(elements[0]),
                elements[2],
                elements[4],
                valueOf(elements[3]),
                Integer.parseInt(elements[5]),
                LocalDateTime.parse(elements[6], Task.dateTimeFormatter),
                Duration.parse(elements[7]));
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
}