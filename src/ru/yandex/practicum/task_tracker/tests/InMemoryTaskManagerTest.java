package ru.yandex.practicum.task_tracker.tests;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.task_tracker.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
    public void createManager() {
        manager = new InMemoryTaskManager();
    }
}
