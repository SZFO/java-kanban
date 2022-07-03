package ru.yandex.practicum.task_tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.managers.InMemoryTaskManager;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.*;

class EpicTest {

    private InMemoryTaskManager inMemoryTaskManager;
    private Epic epic;
    private LocalDateTime time1;
    private LocalDateTime time2;
    private Duration duration1;
    private Duration duration2;

    @BeforeEach
    public void createEpic() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("epic1", "This is Epic 1");
        inMemoryTaskManager.addEpic(epic);
        time1 = LocalDateTime.of(2022, 7, 1, 3, 10, 0);
        duration1 = Duration.ofMinutes(15);
        time2 = LocalDateTime.of(2022, 7, 1, 14, 38, 0);
        duration2 = Duration.ofMinutes(12);
    }

    @Test
    public void calculateEpicStatusIfNoSubtasks() {
        assertEquals(NEW, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksNew() {
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1",
                NEW, epic.getId(), time1, duration1);
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2",
                NEW, epic.getId(), time2, duration2);
        inMemoryTaskManager.addSubTask(subTask2);

        assertEquals(NEW, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksDone() {
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1",
                DONE, epic.getId(), time1, duration1);
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2",
                DONE, epic.getId(), time2, duration2);
        inMemoryTaskManager.addSubTask(subTask2);

        assertEquals(DONE, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfSubtasksNewAndDone() {
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1",
                NEW, epic.getId(), time1, duration1);
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2",
                DONE, epic.getId(), time2, duration2);
        inMemoryTaskManager.addSubTask(subTask2);

        assertEquals(IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksInProgress() {
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1",
                IN_PROGRESS, epic.getId(), time1, duration1);
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2",
                IN_PROGRESS, epic.getId(), time2, duration2);
        inMemoryTaskManager.addSubTask(subTask2);

        assertEquals(IN_PROGRESS, epic.getStatus());
    }
}