package ru.yandex.practicum.task_tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.exceptions.HttpException;
import ru.yandex.practicum.task_tracker.managers.HTTPTaskManager;
import ru.yandex.practicum.task_tracker.managers.Managers;
import ru.yandex.practicum.task_tracker.managers.TaskManager;
import ru.yandex.practicum.task_tracker.server.KVServer;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;
import ru.yandex.practicum.task_tracker.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.*;

public class HTTPTaskManagerTest {
    private static KVServer kvServer;
    private static TaskManager httpTaskManager;

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskManager = Managers.getDefault();
        } catch (IOException e) {
            throw new HttpException("Ошибка при запуске сервера.");
        }
    }

    @AfterEach
    public void stop() {
        kvServer.stop();
    }

    @Test
    public void saveAndLoadTask() {
        Task task1 = new Task("task1", "This is Task 1", IN_PROGRESS,
                LocalDateTime.of(2022, 7, 12, 23, 15), Duration.ofMinutes(25));
        httpTaskManager.addTask(task1);
        HTTPTaskManager loadManager = new HTTPTaskManager("http://localhost:8078/");
        loadManager.load();
        assertEquals(task1, loadManager.getTask(1));
    }

    @Test
    public void saveAndLoadEpicAndSubTask() {
        Epic epic1 = new Epic("epic1", "This is Epic 1");
        httpTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 14, 12, 0), Duration.ofMinutes(30));
        httpTaskManager.addSubTask(subTask1);
        HTTPTaskManager loadManager = new HTTPTaskManager("http://localhost:8078/");
        loadManager.load();
        assertEquals(subTask1, loadManager.getSubTask(2));
    }

    @Test
    public void saveAndLoadHistory() {
        Epic epic1 = new Epic("epic1", "This is Epic 1");
        httpTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 14, 12, 0), Duration.ofMinutes(30));
        httpTaskManager.addSubTask(subTask1);
        Task task1 = new Task("task1", "This is Task 1", IN_PROGRESS,
                LocalDateTime.of(2022, 7, 12, 23, 15), Duration.ofMinutes(25));
        httpTaskManager.addTask(task1);
        httpTaskManager.getTask(3);
        httpTaskManager.getSubTask(2);
        HTTPTaskManager loadManager = new HTTPTaskManager("http://localhost:8078/");
        loadManager.load();
        assertEquals(httpTaskManager.getHistory().size(), loadManager.getHistory().size());
    }
}