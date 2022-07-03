package ru.yandex.practicum.task_tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.task_tracker.managers.TaskManager;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;
import ru.yandex.practicum.task_tracker.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.task_tracker.tasks.TaskStatus.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;
    Task task1;
    Epic epic1;

    @BeforeEach
    public abstract void createManager();

    @BeforeEach
    void createTasksAndEpics() {
        task1 = new Task("task1", "This is Task 1", NEW,
                LocalDateTime.of(2022, 7, 1, 9, 0), Duration.ofMinutes(30));
        epic1 = new Epic("epic1", "This is Epic 1");
    }

    @Test
    void addTaskTest() {
        assertEquals(0, manager.getAllTasks().size());
        manager.addTask(task1);
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    void addEpicTest() {
        assertEquals(0, manager.getAllEpics().size());
        manager.addEpic(epic1);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(NEW, epic1.getStatus());
    }

    @Test
    void addSubTaskTest() {
        assertEquals(0, manager.getAllSubTasks().size());
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(10));
        manager.addSubTask(subTask1);
        assertEquals(1, manager.getAllSubTasks().size());
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        assertEquals(2, manager.getAllSubTasks().size());
        assertEquals(IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void throwAddOrUpdateSubTaskWithoutAddEpicOrIncorrectIdTest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                            LocalDateTime.of(2022, 7, 1, 2, 0),
                            Duration.ofMinutes(60));
                    manager.addSubTask(subTask1);
                });

        assertEquals("Cannot invoke \"java.lang.Integer.intValue()\" because \"this.epicId\" is null",
                exception.getMessage());
    }

    @Test
    void getTaskTest() {
        assertEquals(0, manager.getAllTasks().size());
        assertEquals(0, manager.getHistory().size());
        manager.addTask(task1);
        Task task2 = manager.getTask(task1.getId());
        assertEquals(task1, task2);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void throwGetTaskWithIncorrectIdTest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Task task2 = manager.getTask(5);
                });

        assertEquals("Cannot invoke \"ru.yandex.practicum.task_tracker.tasks.Task.getId()\" " +
                "because \"element\" is null", exception.getMessage());
    }

    @Test
    void getSubTaskTest() {
        assertEquals(0, manager.getAllSubTasks().size());
        assertEquals(0, manager.getHistory().size());
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = manager.getSubTask(subTask1.getId());
        assertEquals(subTask1, subTask2);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void throwGetSubTaskWithIncorrectIdTest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    SubTask subTask2 = manager.getSubTask(7);
                });
        assertEquals("Cannot invoke \"ru.yandex.practicum.task_tracker.tasks.Task.getId()\" " +
                "because \"element\" is null", exception.getMessage());
    }

    @Test
    void getEpicTest() {
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getHistory().size());
        manager.addEpic(epic1);
        Epic epic2 = manager.getEpic(epic1.getId());
        assertEquals(epic1, epic2);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void throwGetEpicWithIncorrectIdTest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Epic epic2 = manager.getEpic(10);
                });
        assertEquals("Cannot invoke \"ru.yandex.practicum.task_tracker.tasks.Task.getId()\" " +
                "because \"element\" is null", exception.getMessage());
    }

    @Test
    void updateTaskTest() {
        manager.addTask(task1);
        Task updatesTask = new Task(task1.getName(), task1.getDescription(), DONE,
                task1.getStartTime(), task1.getDuration());
        manager.updateTask(updatesTask);
        assertEquals(updatesTask, manager.getTask(task1.getId()));
    }

    @Test
    void updateSubTaskTest() {
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask updatedSubTask = new SubTask(subTask1.getName(), subTask1.getDescription(), DONE, epic1.getId(),
                subTask1.getStartTime(), subTask1.getDuration());
        manager.updateSubTask(updatedSubTask);
        assertEquals(updatedSubTask, manager.getSubTask(subTask1.getId()));
        assertEquals(DONE, epic1.getStatus());
    }

    @Test
    void updateEpicTest() {
        manager.addEpic(epic1);
        Epic updatedEpic = new Epic(epic1.getName(), "Change description Epic 1");
        manager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, manager.getEpic(epic1.getId()));
    }

    @Test
    void getEmptyListSubTasksInEpicWithIncorrectIdTest() {
        assertEquals(Collections.emptyList(), manager.getSubTasksInEpic(15));
    }

    @Test
    void getSubTasksInEpicTest() {
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        SubTask[] subTaskArray1 = {subTask1, subTask2};
        SubTask[] subTaskArray2 = manager.getAllSubTasks().toArray(new SubTask[0]);
        assertArrayEquals(subTaskArray1, subTaskArray2);
    }

    @Test
    void deleteTaskTest() {
        assertTrue(manager.getAllTasks().isEmpty());
        manager.addTask(task1);
        assertEquals(1, manager.getAllTasks().size());
        manager.deleteTask(task1.getId());
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void throwDeleteTaskIfIncorrectIdTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.deleteTask(16));

        assertEquals("Задача с указанным Id отсутствует", exception.getMessage());
    }

    @Test
    void deleteSubTaskTest() {
        assertTrue(manager.getAllSubTasks().isEmpty());
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        assertEquals(2, manager.getAllSubTasks().size());
        assertEquals(IN_PROGRESS, epic1.getStatus());
        manager.deleteSubTask(subTask2.getId());
        assertEquals(1, manager.getAllSubTasks().size());
        assertEquals(DONE, epic1.getStatus());
    }

    @Test
    void throwDeleteSubTaskIfIncorrectIdTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.deleteSubTask(16));

        assertEquals("Подзадача с указанным Id отсутствует", exception.getMessage());
    }

    @Test
    void deleteEpicTest() {
        assertTrue(manager.getAllEpics().isEmpty());
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubTasks().size());
        assertEquals(DONE, epic1.getStatus());
        manager.deleteEpic(epic1.getId());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    void throwDeleteEpicIfIncorrectIdTest() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> manager.deleteEpic(16));

        assertEquals("Эпик с указанным Id отсутствует", exception.getMessage());
    }

    @Test
    void deleteAllTasksTest() {
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
        manager.addTask(task1);
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        assertEquals(1, manager.getAllTasks().size());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubTasks().size());
        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubTasks().isEmpty());
    }

    @Test
    void getAllTasksTest() {
        assertTrue(manager.getAllTasks().isEmpty());
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        manager.addTask(task1);
        assertEquals(tasks, manager.getAllTasks());
    }

    @Test
    void getAllSubTasksTest() {
        assertTrue(manager.getAllSubTasks().isEmpty());
        manager.addEpic(epic1);
        List<SubTask> subTasks = new ArrayList<>();
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(subTasks, manager.getAllSubTasks());
    }

    @Test
    void getAllEpicsTest() {
        assertTrue(manager.getAllEpics().isEmpty());
        List<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        manager.addEpic(epic1);
        assertEquals(epics, manager.getAllEpics());
    }

    @Test
    void getHistoryTest() {
        assertTrue(manager.getHistory().isEmpty());
        manager.addTask(task1);
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        manager.getEpic(epic1.getId());
        manager.getTask(task1.getId());
        manager.getSubTask(subTask1.getId());
        assertEquals(3, manager.getHistory().size());
    }

    @Test
    void getPrioritizedTasksTest() {
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        manager.addTask(task1);
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        // Добавляем в тестовый список задачи вручную в хронологическом порядке по startTime
        List<Task> temp = new LinkedList<>();
        temp.add(subTask1);
        temp.add(task1);
        temp.add(subTask2);
        assertEquals(temp, manager.getPrioritizedTasks().stream().toList());
    }

    @Test
    void tasksTimeValidationTest() {
        manager.addTask(task1);
        Task task2 = new Task("task2", "This is Task 2", IN_PROGRESS,
                LocalDateTime.of(2022, 7, 1, 9, 20), Duration.ofMinutes(20));
        //  Время старта задачи №2 находится в интервале задачи №1
        final ManagerSaveException exception1 = assertThrows(
                ManagerSaveException.class,
                () -> manager.addTask(task2));

        assertEquals("При создании задачи обнаружено пересечение по времени выполнения.",
                exception1.getMessage());

        Task task3 = new Task("task3", "This is Task 3", NEW,
                LocalDateTime.of(2022, 7, 1, 17, 10), Duration.ofMinutes(50));
        manager.addTask(task3);
        Task task4 = new Task("task4", "This is Task 2", DONE,
                LocalDateTime.of(2022, 7, 1, 18, 30), Duration.ofMinutes(30));
        manager.addTask(task4);
        // Обновим задачу №4, указав новое описание и новое время старта, совпадающее с временем старта задачи №3
        final ManagerSaveException exception2 = assertThrows(
                ManagerSaveException.class,
                () -> manager.updateTask(new Task(task4.getName(), "This is New SubTask 4", task4.getStatus(),
                        LocalDateTime.of(2022, 7, 1, 17, 10), task4.getDuration())));

        assertEquals("При обновлении задачи обнаружено пересечение по времени выполнения.",
                exception2.getMessage());
    }

    @Test
    void subTasksTimeValidationTest() {
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 2, 0), Duration.ofMinutes(20));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 2, 15), Duration.ofMinutes(20));
        //  Время старта подзадачи №2 находится в интервале подзадачи №1
        final ManagerSaveException exception1 = assertThrows(
                ManagerSaveException.class,
                () -> manager.addSubTask(subTask2));

        assertEquals("При создании подзадачи обнаружено пересечение по времени выполнения.",
                exception1.getMessage());

        SubTask subTask3 = new SubTask("subTask3", "This is SubTask 3", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 6, 0), Duration.ofMinutes(40));
        manager.addSubTask(subTask3);
        SubTask subTask4 = new SubTask("subTask4", "This is SubTask 4", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 8, 0), Duration.ofMinutes(10));
        manager.addSubTask(subTask4);
        // Обновим подзадачу №4, указав новое описание и новое время старта, совпадающее с временем старта подзадачи №3
        final ManagerSaveException exception2 = assertThrows(
                ManagerSaveException.class,
                () -> manager.updateSubTask(new SubTask(subTask4.getName(), "This is New SubTask 4",
                        subTask4.getStatus(), subTask4.getEpicId(),
                        LocalDateTime.of(2022, 7, 2, 6, 0),
                        subTask4.getDuration())));

        assertEquals("При обновлении подзадачи обнаружено пересечение по времени выполнения.",
                exception2.getMessage());
    }
}