package ru.yandex.practicum.task_tracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class SubTask extends Task {
    private transient Integer id;
    private Integer epicId;

    public SubTask(String name, String description, TaskStatus status, Integer epicId,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(Integer id, String name, String description, TaskStatus status,
                   Integer epicId, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.id = id;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
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
        return Objects.equals(super.getId(), subTask.getId()) &&
                Objects.equals(super.getName(), subTask.getName()) &&
                Objects.equals(super.getDescription(), subTask.getDescription()) &&
                Objects.equals(super.getStatus(), subTask.getStatus());
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                getEpicId() + "," + (Optional.ofNullable(getStartTime()).isPresent() ?
                getStartTime().format(DateTimeFormat.getDateTimeFormatter()) : "Not set") + "," +
                (Optional.ofNullable(getDuration()).isPresent() ?
                        getDuration() : "Not set") + "," +
                (Optional.ofNullable(getEndTime()).isPresent() ?
                        getEndTime().format(DateTimeFormat.getDateTimeFormatter()) : "Missing");
    }
}