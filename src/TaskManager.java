import java.util.List;

public interface TaskManager {

    public void printAllTasks();
    public void addTask(Task task);
    public void addEpic(Epic epic);
    public void addSubTask(SubTask subTask);
    public Task getTask(Integer id);
    public void updateTask(Task task, Integer id, TaskStatus status);
    public void updateSubTask(SubTask subTask, Integer id, TaskStatus status);
    public void updateEpic(Epic epic);
    public String getSubTasksInEpic(Epic epic);
    public void deleteTask(Integer id);
    public void deleteSubTask(Integer id);
    public void deleteEpic(Integer id);
    public void deleteAllTasks();
    public TaskStatus calculateEpicStatus(Epic epic);
    public List<Task> getHistory();

}
