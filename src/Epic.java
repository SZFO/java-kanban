import java.util.*;

public class Epic extends Task {
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;
    List<Integer> subTasks;

    public Epic(String taskName, String taskDescription) { // Конструктор для создания эпика
        super(taskName, taskDescription);
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = TaskStatus.NEW;
        this.subTasks = new ArrayList<>();
    }

    public Epic(Integer taskId, String taskName, String taskDescription) { // Конструктор для обновления эпика
        super(taskName, taskDescription);
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }


    @Override
    public Integer getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        this.subTasks = subTasks;
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
        Epic epic = (Epic) obj;
        return Objects.equals(taskId, epic.taskId) &&
                Objects.equals(taskName, epic.taskName) &&
                Objects.equals(taskDescription, epic.taskDescription) &&
                Objects.equals(taskStatus, epic.taskStatus);
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "Название эпика='" + taskName + '\'' +
                ", Описание эпика='" + taskDescription + '\'' +
                ", Статус эпика=" + taskStatus +
                ", ID эпика=" + taskId +
                '}';
    }

}
