package managers.memory;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public interface TaskManager {

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTask(Integer id);

    SubTask getSubTask(Integer id);

    Epic getEpic(Integer id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    String subTasksInEpicToString(Epic epic);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    void deleteAllTasks();

    List<Task> getHistory();

}
