import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.main.java.history.HistoryManager;
import ru.yandex.practicum.task_tracker.main.java.history.InMemoryHistoryManager;
import ru.yandex.practicum.task_tracker.main.java.tasks.Epic;
import ru.yandex.practicum.task_tracker.main.java.tasks.SubTask;
import ru.yandex.practicum.task_tracker.main.java.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.task_tracker.main.java.tasks.TaskStatus.NEW;

public class HistoryManagerTest {

    private Task task1;
    private Epic epic1;
    private SubTask subTask1;
    private HistoryManager manager;

    @BeforeEach
    public void createHistoryManager() {
        manager = new InMemoryHistoryManager();
    }

    @BeforeEach
    public void createTasks() {
        task1 = new Task("task1", "This is Task 1", NEW,
                LocalDateTime.of(2022, 7, 1, 9, 0), Duration.ofMinutes(30));
        task1.setId(1);
        epic1 = new Epic("epic1", "This is Epic 1");
        epic1.setId(2);
        subTask1 = new SubTask("subTask1", "This is SubTask 1", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(10));
        subTask1.setId(3);
    }

    @Test
    public void addTaskInHistoryIfSheEmptyTest() {
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void addDuplicationTaskInHistoryTest() {
        manager.add(task1);
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void removeFirstTest() {
        manager.add(task1);
        manager.add(epic1);
        manager.remove(task1.getId());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void removeMiddleTest() {
        manager.add(epic1);
        manager.add(subTask1);
        manager.add(task1);
        manager.remove(subTask1.getId());
        assertEquals(2, manager.getHistory().size());
    }

    @Test
    public void removeLastTest() {
        manager.add(epic1);
        manager.add(subTask1);
        manager.add(task1);
        manager.remove(task1.getId());
        assertEquals(2, manager.getHistory().size());
    }
}