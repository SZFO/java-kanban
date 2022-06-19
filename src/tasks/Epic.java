package tasks;

import java.util.*;

public class Epic extends Task {

    private List<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTasks(SubTask subTask) {
        subTasks.add(subTask);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
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
        Epic epic = (Epic) obj;
        return Objects.equals(super.getId(), epic.getId()) && Objects.equals(super.getName(), epic.getName()) && Objects.equals(super.getDescription(), epic.getDescription()) && Objects.equals(super.getStatus(), epic.getStatus());
    }

    @Override
    public String toString() {
        return "Эпик{" + "Название эпика='" + super.getName() + '\'' + ", Описание эпика='" + super.getDescription() + '\'' + ", Статус эпика='" + super.getStatus() + '\'' + ", ID эпика='" + super.getId() + '\'' + '}';
    }

    public void calculateEpicStatus() {
        int amountStatusNew = 0;
        int amountStatusDone = 0;
        for (SubTask subTask : getSubTasks()) {
            if (subTask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                setStatus(TaskStatus.IN_PROGRESS);
            }
            if (subTask.getStatus().equals(TaskStatus.DONE)) {
                amountStatusDone++;
            }
            if (subTask.getStatus().equals(TaskStatus.NEW)) {
                amountStatusNew++;
            }
        }
        if (amountStatusDone == getSubTasks().size() && !getSubTasks().isEmpty()) {
            setStatus(TaskStatus.DONE);
        } else if (amountStatusDone > 0 && amountStatusDone < getSubTasks().size()) {
            setStatus(TaskStatus.IN_PROGRESS);
        } else if (getSubTasks().isEmpty() || amountStatusNew == getSubTasks().size()) {
            setStatus(TaskStatus.NEW);
        }
    }
}
