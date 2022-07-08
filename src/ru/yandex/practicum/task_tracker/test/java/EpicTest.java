import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.main.java.managers.InMemoryTaskManager;
import ru.yandex.practicum.task_tracker.main.java.tasks.Epic;
import ru.yandex.practicum.task_tracker.main.java.tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.task_tracker.main.java.tasks.TaskStatus.*;

public class EpicTest {

    private InMemoryTaskManager manager;
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    public void createTasks() {
        manager = new InMemoryTaskManager();
        epic = new Epic("epic1", "This is Epic 1");
        manager.addEpic(epic);
        subTask1 = new SubTask("subTask1", "This is SubTask 1",
                NEW, epic.getId(), LocalDateTime.of(2022, 7, 1, 3, 10, 0),
                Duration.ofMinutes(15));
        manager.addSubTask(subTask1);
        subTask2 = new SubTask("subTask2", "This is SubTask 2",
                NEW, epic.getId(), LocalDateTime.of(2022, 7, 1, 14, 38, 0),
                Duration.ofMinutes(12));
        manager.addSubTask(subTask2);
    }

    @Test
    public void calculateEpicStatusIfNoSubtasks() {
        manager.deleteSubTask(subTask1.getId());
        manager.deleteSubTask(subTask2.getId());
        assertEquals(NEW, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksNew() {
        assertEquals(NEW, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksDone() {
        subTask1.setStatus(DONE);
        subTask2.setStatus(DONE);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        assertEquals(DONE, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfSubtasksNewAndDone() {
        subTask2.setStatus(DONE);
        manager.updateSubTask(subTask2);
        assertEquals(IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void calculateEpicStatusIfAllSubtasksInProgress() {
        subTask1.setStatus(IN_PROGRESS);
        subTask2.setStatus(IN_PROGRESS);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        assertEquals(IN_PROGRESS, epic.getStatus());
    }
}