package ru.yandex.practicum.task_tracker.tasks;

import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.*;
import static ru.yandex.practicum.task_tracker.tasks.TaskType.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Integer id;
    private List<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
        this.endTime = getEndTime();
    }

    public Epic(Integer id, String name, String description) {
        super(name, description);
        this.id = id;
        this.subTasks = new ArrayList<>();
        this.endTime = getEndTime();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        return EPIC;
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
        return Objects.equals(super.getId(), epic.getId()) &&
                Objects.equals(super.getName(), epic.getName()) &&
                Objects.equals(super.getDescription(), epic.getDescription()) &&
                Objects.equals(super.getStatus(), epic.getStatus());
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," +
                (Optional.ofNullable(getStartTime()).isPresent() ?
                        getStartTime().format(dateTimeFormatter) : "Not set") + "," +
                (Optional.ofNullable(getDuration()).isPresent() ?
                        getDuration() : "Not set") + "," +
                (Optional.ofNullable(getEndTime()).isPresent() ?
                        getEndTime().format(dateTimeFormatter) : "Missing");
    }

    public void calculateEpicStatus() {
        int amountStatusNew = 0;
        int amountStatusDone = 0;
        for (SubTask subTask : getSubTasks()) {
            if (subTask.getStatus().equals(IN_PROGRESS)) {
                setStatus(IN_PROGRESS);
            }
            if (subTask.getStatus().equals(DONE)) {
                amountStatusDone++;
            }
            if (subTask.getStatus().equals(NEW)) {
                amountStatusNew++;
            }
        }
        if (amountStatusDone == getSubTasks().size() && !getSubTasks().isEmpty()) {
            setStatus(DONE);
        } else if (amountStatusDone > 0 && amountStatusDone < getSubTasks().size()) {
            setStatus(IN_PROGRESS);
        } else if (getSubTasks().isEmpty() || amountStatusNew == getSubTasks().size()) {
            setStatus(NEW);
        }
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return null;
        }
        LocalDateTime startTime = LocalDateTime.MAX;
        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime() == null) {
                continue;
            }
            if (subTask.getStartTime() != null && startTime.isAfter(subTask.getStartTime())) {
                startTime = subTask.getStartTime();
            }
        }
        return startTime;
    }

    @Override
    public Duration getDuration() {
        if (subTasks.isEmpty()) {
            return null;
        }
        Duration duration = Duration.ZERO;
        for (SubTask subTask : subTasks) {
            if (subTask.getDuration() != null) {
                duration = duration.plus(subTask.getDuration());
            }
        }
        return duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subTasks.isEmpty()) {
            return null;
        }
        LocalDateTime temp = LocalDateTime.MIN;
        for (SubTask subTask : subTasks) {
            if (subTask.getEndTime() == null) {
                continue;
            }
            if (subTask.getEndTime() != null && temp.isBefore(subTask.getEndTime())) {
                temp = subTask.getEndTime();
            }
        }
        return temp;
    }
}