import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int VIEW_LIST_SIZE = 10;
    private List<Task> taskViewsHistory;

    public InMemoryHistoryManager() {
        this.taskViewsHistory = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        taskViewsHistory.add(task);
        if (taskViewsHistory.size() > VIEW_LIST_SIZE) {
            taskViewsHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskViewsHistory;
    }

}
