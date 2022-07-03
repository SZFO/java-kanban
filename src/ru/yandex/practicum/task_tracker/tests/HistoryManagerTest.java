package ru.yandex.practicum.task_tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.history.HistoryManager;
import ru.yandex.practicum.task_tracker.history.InMemoryHistoryManager;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;
import ru.yandex.practicum.task_tracker.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.NEW;

public class HistoryManagerTest {

    Task task1;
    Epic epic1;
    HistoryManager manager;

    @BeforeEach
    void createHistoryManager() {
        manager = new InMemoryHistoryManager();
    }

    @BeforeEach
    void createTasksAndEpics() {
        task1 = new Task("task1", "This is Task 1", NEW,
                LocalDateTime.of(2022, 7, 1, 9, 0), Duration.ofMinutes(30));
        task1.setId(1);
        epic1 = new Epic("epic1", "This is Epic 1");
        epic1.setId(2);
    }

    @Test
    void addTaskInHistoryIfSheEmptyTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void addDuplicationTaskInHistoryTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.add(task1);
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void removeFirstTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.add(task1);
        manager.add(epic1);
        assertEquals(2, manager.getHistory().size());
        manager.remove(task1.getId());
        assertEquals(epic1, manager.getHistory().get(0));
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void removeMiddleTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.add(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(10));
        subTask1.setId(3);
        manager.add(subTask1);
        manager.add(task1);
        assertEquals(3, manager.getHistory().size());
        manager.remove(subTask1.getId());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    void removeLastTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.add(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(10));
        subTask1.setId(3);
        manager.add(subTask1);
        manager.add(task1);
        assertEquals(3, manager.getHistory().size());
        manager.remove(task1.getId());
        assertEquals(subTask1, manager.getHistory().get(0));
        assertEquals(2, manager.getHistory().size());
    }
}