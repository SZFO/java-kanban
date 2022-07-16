package ru.yandex.practicum.task_tracker.main.java.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.task_tracker.main.java.server.KVTaskClient;
import ru.yandex.practicum.task_tracker.main.java.server.adapters.DurationAdapter;
import ru.yandex.practicum.task_tracker.main.java.server.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.task_tracker.main.java.tasks.Epic;
import ru.yandex.practicum.task_tracker.main.java.tasks.SubTask;
import ru.yandex.practicum.task_tracker.main.java.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private final Gson gson;

    public HTTPTaskManager(String url) {
        client = new KVTaskClient(url);
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void save() {
        String taskToGson = gson.toJson(getTasks());
        String epicToGson = gson.toJson(getEpics());
        String subTaskToGson = gson.toJson(getSubTasks());
        String historyToGson = gson.toJson(getHistory());
        client.put("task", taskToGson);
        client.put("epic", epicToGson);
        client.put("subtask", subTaskToGson);
        client.put("history", historyToGson);
    }

    public void load() {
        setTasks(gson.fromJson(client.load("task"), new TypeToken<HashMap<Integer, Task>>() {
        }.getType()));
        setEpics(gson.fromJson(client.load("epic"), new TypeToken<HashMap<Integer, Epic>>() {
        }.getType()));
        setSubTasks(gson.fromJson(client.load("subtask"), new TypeToken<HashMap<Integer, SubTask>>() {
        }.getType()));
        ArrayList<Task> loadHistoryList = new ArrayList<>(gson.fromJson(client.load("history"),
                new TypeToken<ArrayList<Task>>() {
                }.getType()));
        for (Task taskInHistory : loadHistoryList) {
            getHistoryManager().add(taskInHistory);
        }
    }
}