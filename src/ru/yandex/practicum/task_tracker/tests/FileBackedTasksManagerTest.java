package ru.yandex.practicum.task_tracker.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.exceptions.ManagerSaveException;
import ru.yandex.practicum.task_tracker.managers.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static final String BACKED_TASKS_TESTS_PATH = "resources/text-files/tasks1.csv";
    private static final String ERROR_MESSAGE = "������ ������ �� ����� �� ����� �����.";

    @BeforeEach
    @Override
    public void createManager() {
        File file = new File(BACKED_TASKS_TESTS_PATH);
        manager = new FileBackedTasksManager(file);
    }

    @Test
    void saveIfEmptyTasksListTest() {
        // ���� ������ �� �������, �� � ���� ����������� ����� � ������ ������. �� ������ ����� ������ == 2.
        manager.save();
        try {
            assertEquals(2, Files.readAllLines(Paths.get(BACKED_TASKS_TESTS_PATH)).size());
        } catch (IOException e) {
            throw new ManagerSaveException(ERROR_MESSAGE);
        }
    }

    @Test
    void saveInEmptyHistoryListTest() {
        manager.addEpic(epic1);
        manager.getEpic(epic1.getId());
        try {
            Assertions.assertEquals("1", Files.readAllLines(Paths.get(BACKED_TASKS_TESTS_PATH)).get(3));
        } catch (IOException e) {
            throw new ManagerSaveException(ERROR_MESSAGE);
        }
    }

    @Test
    void saveEpicWithoutSubTasksTest() {
        manager.addEpic(epic1);
        String testString;
        try {
            testString = Files.readAllLines(Paths.get(BACKED_TASKS_TESTS_PATH)).get(1);
        } catch (IOException e) {
            throw new ManagerSaveException(ERROR_MESSAGE);
        }
        assertEquals("1,EPIC,epic1,NEW,This is Epic 1,Not set,Not set,Missing", testString);
    }
}