package ru.yandex.practicum.task_tracker;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.task_tracker.managers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
    public void createManager() {
        manager = new InMemoryTaskManager();
    }
}