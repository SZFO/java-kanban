package managers;

import managers.history.HistoryManager;
import managers.history.InMemoryHistoryManager;
import managers.memory.InMemoryTaskManager;
import managers.memory.TaskManager;

public abstract class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
