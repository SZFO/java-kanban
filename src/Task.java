import java.util.Objects;

public class Task {
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;

    public Task(String taskName, String taskDescription) { // Конструктор для создания задачи
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(Integer taskId, String taskName, String taskDescription, TaskStatus taskStatus) { // Конструктор для обновления задачи
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }


    public Integer getTaskId() {
        return taskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (taskId != 0) {
            hash = hash + taskId.hashCode();
        }
        hash = hash * 31;

        if (taskName != null) {
            hash = hash + taskName.hashCode();
        }
        hash = hash * 31;

        if (taskDescription != null) {
            hash = hash + taskDescription.hashCode();
        }
        hash = hash * 31;

        if (taskStatus != null) {
            hash = hash + taskStatus.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(taskId, task.taskId) &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) &&
                Objects.equals(taskStatus, task.taskStatus);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Статус задачи=" + taskStatus +
                ", ID задачи=" + taskId +
                '}';
    }
}
