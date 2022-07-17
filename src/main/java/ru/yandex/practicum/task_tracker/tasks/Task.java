package ru.yandex.practicum.task_tracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, String name, String description, TaskStatus status,
                LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != 0) {
            hash = hash + id.hashCode();
        }
        hash = hash * 31;

        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }
        hash = hash * 31;

        if (status != null) {
            hash = hash + status.hashCode();
        }
        return hash;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                (Optional.ofNullable(getStartTime()).isPresent() ?
                        getStartTime().format(DateTimeFormat.getDateTimeFormatter()) : "Not set") + "," +
                (Optional.ofNullable(getDuration()).isPresent() ?
                        getDuration() : "Not set") + "," +
                (Optional.ofNullable(getEndTime()).isPresent() ?
                        getEndTime().format(DateTimeFormat.getDateTimeFormatter()) : "Missing");
    }
}