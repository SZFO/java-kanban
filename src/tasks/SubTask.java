package tasks;

import java.util.Objects;

public class SubTask extends Task {
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic) { // Конструктор для создания подзадачи
        super(name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (super.getId() != 0) {
            hash = hash + super.getId().hashCode();
        }
        hash = hash * 31;

        if (super.getName() != null) {
            hash = hash + super.getName().hashCode();
        }
        hash = hash * 31;

        if (super.getDescription() != null) {
            hash = hash + super.getDescription().hashCode();
        }
        hash = hash * 31;

        if (super.getStatus() != null) {
            hash = hash + super.getStatus().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SubTask subTask = (SubTask) obj;
        return Objects.equals(super.getId(), subTask.getId()) && Objects.equals(super.getName(), subTask.getName()) && Objects.equals(super.getDescription(), subTask.getDescription()) && Objects.equals(super.getStatus(), subTask.getStatus());
    }

    @Override
    public String toString() {
        return "Подзадача{" + "Название подзадачи='" + super.getName() + '\'' + ", Описание подзадачи='" + super.getDescription() + '\'' + ", Статус подзадачи=" + super.getStatus() + ", ID подзадачи=" + super.getId() + ", Входит в эпик='" + epic.getName() + '\'' + '}';
    }
}
