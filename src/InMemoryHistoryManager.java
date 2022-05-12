import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> taskViewsHistory;

    public InMemoryHistoryManager() {
        this.taskViewsHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        taskViewsHistory.add(task);
        if(taskViewsHistory.size() > 10){
            taskViewsHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {  //Получение списка просмотренных задач
        return taskViewsHistory;
    }

}
