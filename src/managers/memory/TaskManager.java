package managers.memory;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getAllTasks();

    Map<Integer, SubTask> getAllSubTasks();

    Map<Integer, Epic> getAllEpics();

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

    List<Task> getAllTasksList();

    List<SubTask> getAllSubTasksList();

    List<Epic> getAllEpicsList();

}
