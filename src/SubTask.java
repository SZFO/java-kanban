import java.util.Objects;

public class SubTask extends Task {
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;
    private Integer epicId;

    public SubTask(String taskName, String taskDescription) { // Конструктор для создания подзадачи
        super(taskName, taskDescription);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
    }

    public SubTask(Integer taskId, String taskName, String taskDescription, TaskStatus taskStatus) { // Конструктор для обновления подзадачи
        super(taskId, taskName, taskDescription, taskStatus);
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    @Override
    public Integer getTaskId() {
        return taskId;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
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
        SubTask subTask = (SubTask) obj;
        return Objects.equals(taskId, subTask.taskId) &&
                Objects.equals(taskName, subTask.taskName) &&
                Objects.equals(taskDescription, subTask.taskDescription) &&
                Objects.equals(taskStatus, subTask.taskStatus);
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "Название подзадачи='" + taskName + '\'' +
                ", Описание подзадачи='" + taskDescription + '\'' +
                ", Статус подзадачи=" + taskStatus +
                ", ID подзадачи=" + taskId +
                '}';
    }

}
