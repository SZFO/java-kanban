import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.task_tracker.main.java.managers.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
    public void createManager() {
        manager = new InMemoryTaskManager();
    }
}