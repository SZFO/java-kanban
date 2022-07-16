import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.task_tracker.main.java.exceptions.HttpException;
import ru.yandex.practicum.task_tracker.main.java.managers.Managers;
import ru.yandex.practicum.task_tracker.main.java.managers.TaskManager;
import ru.yandex.practicum.task_tracker.main.java.server.HttpTaskServer;
import ru.yandex.practicum.task_tracker.main.java.server.KVServer;
import ru.yandex.practicum.task_tracker.main.java.server.adapters.DurationAdapter;
import ru.yandex.practicum.task_tracker.main.java.server.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.task_tracker.main.java.tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.task_tracker.main.java.tasks.TaskStatus.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HTTPTaskServerTest {
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;
    private static TaskManager manager;

    @BeforeEach
    public void start() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            manager = Managers.getDefault();
            httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();
        } catch (IOException e) {
            throw new HttpException("Ошибка при запуске сервера.");
        }
    }

    @AfterEach
    public void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void GETAllTasksIfTheyMissing() {
        assertEquals("[]", sendGetRequest("tasks/task/").body());
    }

    @Test
    public void GETTasksById() {
        addTasks();
        String jsonTask2 = """
                {
                  "id": 2,
                  "name": "task2",
                  "description": "This is Task 2",
                  "status": "NEW",
                  "startTime": "2022-07-10T05:30:00",
                  "duration": 15,
                  "endTime": "2022-07-10T05:45:00"
                }""";
        assertEquals(jsonTask2, sendGetRequest("tasks/task/?id=2").body());
    }

    @Test
    public void GETAllTasks() {
        addTasks();
        String jsonAllTasks = """
                [
                  {
                    "id": 1,
                    "name": "task1",
                    "description": "This is Task 1",
                    "status": "DONE",
                    "startTime": "2022-07-01T09:00:00",
                    "duration": 30,
                    "endTime": "2022-07-01T09:30:00"
                  },
                  {
                    "id": 2,
                    "name": "task2",
                    "description": "This is Task 2",
                    "status": "NEW",
                    "startTime": "2022-07-10T05:30:00",
                    "duration": 15,
                    "endTime": "2022-07-10T05:45:00"
                  }
                ]""";
        assertEquals(jsonAllTasks, sendGetRequest("tasks/task/").body());
    }

    @Test
    public void GETEpicsIfTheyMissing() {
        assertEquals("[]", sendGetRequest("tasks/epic/").body());
    }

    @Test
    public void GETEpicsById() {
        addEpicsAndSubTasks();
        String jsonEpic1 = """
                {
                  "subTasks": [
                    {
                      "epicId": 1,
                      "id": 4,
                      "name": "subTask1",
                      "description": "This is SubTask 1",
                      "status": "DONE",
                      "startTime": "2022-07-01T02:00:00",
                      "duration": 60,
                      "endTime": "2022-07-01T03:00:00"
                    },
                    {
                      "epicId": 1,
                      "id": 5,
                      "name": "subTask2",
                      "description": "This is SubTask 2",
                      "status": "NEW",
                      "startTime": "2022-07-02T04:40:00",
                      "duration": 20,
                      "endTime": "2022-07-02T05:00:00"
                    }
                  ],
                  "id": 1,
                  "name": "epic1",
                  "description": "This is Epic 1",
                  "status": "IN_PROGRESS",
                  "startTime": "2022-07-01T02:00:00",
                  "duration": 80,
                  "endTime": "2022-07-02T05:00:00"
                }""";
        assertEquals(jsonEpic1, sendGetRequest("tasks/epic/?id=1").body());
    }

    @Test
    public void GETAllEpics() {
        addEpicsAndSubTasks();
        String jsonAllEpics = """
                [
                  {
                    "subTasks": [
                      {
                        "epicId": 1,
                        "id": 4,
                        "name": "subTask1",
                        "description": "This is SubTask 1",
                        "status": "DONE",
                        "startTime": "2022-07-01T02:00:00",
                        "duration": 60,
                        "endTime": "2022-07-01T03:00:00"
                      },
                      {
                        "epicId": 1,
                        "id": 5,
                        "name": "subTask2",
                        "description": "This is SubTask 2",
                        "status": "NEW",
                        "startTime": "2022-07-02T04:40:00",
                        "duration": 20,
                        "endTime": "2022-07-02T05:00:00"
                      }
                    ],
                    "id": 1,
                    "name": "epic1",
                    "description": "This is Epic 1",
                    "status": "IN_PROGRESS",
                    "startTime": "2022-07-01T02:00:00",
                    "duration": 80,
                    "endTime": "2022-07-02T05:00:00"
                  },
                  {
                    "subTasks": [
                      {
                        "epicId": 2,
                        "id": 6,
                        "name": "subTask3",
                        "description": "This is SubTask 3",
                        "status": "DONE",
                        "startTime": "2022-07-12T15:40:00",
                        "duration": 20,
                        "endTime": "2022-07-12T16:00:00"
                      }
                    ],
                    "id": 2,
                    "name": "epic2",
                    "description": "This is Epic 2",
                    "status": "DONE",
                    "startTime": "2022-07-12T15:40:00",
                    "duration": 20,
                    "endTime": "2022-07-12T16:00:00"
                  },
                  {
                    "subTasks": [],
                    "id": 3,
                    "name": "epic3",
                    "description": "This is Epic 3",
                    "status": "NEW"
                  }
                ]""";
        assertEquals(jsonAllEpics, sendGetRequest("tasks/epic/").body());
    }

    @Test
    public void GETSubTasksIfTheyMissing() {
        assertEquals("[]", sendGetRequest("tasks/subtask/").body());
    }

    @Test
    public void GETSubTasksById() {
        addEpicsAndSubTasks();
        String jsonSubtask5 = """
                {
                  "epicId": 1,
                  "id": 5,
                  "name": "subTask2",
                  "description": "This is SubTask 2",
                  "status": "NEW",
                  "startTime": "2022-07-02T04:40:00",
                  "duration": 20,
                  "endTime": "2022-07-02T05:00:00"
                }""";
        assertEquals(jsonSubtask5, sendGetRequest("tasks/subtask/?id=5").body());
    }

    @Test
    public void GETAllSubTasks() {
        addEpicsAndSubTasks();
        String jsonAllSubTasks = """
                [
                  {
                    "epicId": 1,
                    "id": 4,
                    "name": "subTask1",
                    "description": "This is SubTask 1",
                    "status": "DONE",
                    "startTime": "2022-07-01T02:00:00",
                    "duration": 60,
                    "endTime": "2022-07-01T03:00:00"
                  },
                  {
                    "epicId": 1,
                    "id": 5,
                    "name": "subTask2",
                    "description": "This is SubTask 2",
                    "status": "NEW",
                    "startTime": "2022-07-02T04:40:00",
                    "duration": 20,
                    "endTime": "2022-07-02T05:00:00"
                  },
                  {
                    "epicId": 2,
                    "id": 6,
                    "name": "subTask3",
                    "description": "This is SubTask 3",
                    "status": "DONE",
                    "startTime": "2022-07-12T15:40:00",
                    "duration": 20,
                    "endTime": "2022-07-12T16:00:00"
                  }
                ]""";
        assertEquals(jsonAllSubTasks, sendGetRequest("tasks/subtask/").body());
    }

    @Test
    public void GETSubTasksInEpic() {
        addEpicsAndSubTasks();
        String jsonSubtasksInEpic1 = """
                [
                  {
                    "epicId": 1,
                    "id": 4,
                    "name": "subTask1",
                    "description": "This is SubTask 1",
                    "status": "DONE",
                    "startTime": "2022-07-01T02:00:00",
                    "duration": 60,
                    "endTime": "2022-07-01T03:00:00"
                  },
                  {
                    "epicId": 1,
                    "id": 5,
                    "name": "subTask2",
                    "description": "This is SubTask 2",
                    "status": "NEW",
                    "startTime": "2022-07-02T04:40:00",
                    "duration": 20,
                    "endTime": "2022-07-02T05:00:00"
                  }
                ]""";
        assertEquals(jsonSubtasksInEpic1, sendGetRequest("tasks/subtask/epic/?id=1").body());
    }

    @Test
    public void GETTasksPriorityIfTheyMissing() {
        assertEquals("[]", sendGetRequest("tasks").body());
    }

    @Test
    public void GETTasksPriority() {
        addTasks();
        addEpicsAndSubTasks();
        String jsonPriorityTasks = """
                [
                  {
                    "epicId": 3,
                    "id": 6,
                    "name": "subTask1",
                    "description": "This is SubTask 1",
                    "status": "DONE",
                    "startTime": "2022-07-01T02:00:00",
                    "duration": 60,
                    "endTime": "2022-07-01T03:00:00"
                  },
                  {
                    "id": 1,
                    "name": "task1",
                    "description": "This is Task 1",
                    "status": "DONE",
                    "startTime": "2022-07-01T09:00:00",
                    "duration": 30,
                    "endTime": "2022-07-01T09:30:00"
                  },
                  {
                    "epicId": 3,
                    "id": 7,
                    "name": "subTask2",
                    "description": "This is SubTask 2",
                    "status": "NEW",
                    "startTime": "2022-07-02T04:40:00",
                    "duration": 20,
                    "endTime": "2022-07-02T05:00:00"
                  },
                  {
                    "id": 2,
                    "name": "task2",
                    "description": "This is Task 2",
                    "status": "NEW",
                    "startTime": "2022-07-10T05:30:00",
                    "duration": 15,
                    "endTime": "2022-07-10T05:45:00"
                  },
                  {
                    "epicId": 4,
                    "id": 8,
                    "name": "subTask3",
                    "description": "This is SubTask 3",
                    "status": "DONE",
                    "startTime": "2022-07-12T15:40:00",
                    "duration": 20,
                    "endTime": "2022-07-12T16:00:00"
                  }
                ]""";
        assertEquals(jsonPriorityTasks, sendGetRequest("tasks").body());
    }

    @Test
    public void GETHistoryIfSheEmpty() {
        assertEquals("[]", sendGetRequest("tasks/history").body());
    }

    @Test
    public void GETHistory() {
        addTasks();
        addEpicsAndSubTasks();
        sendGetRequest("tasks/task/?id=2");
        sendGetRequest("tasks/epic/?id=5");
        sendGetRequest("tasks/subtask/?id=8");
        String jsonHistory = """
                [
                  {
                    "epicId": 4,
                    "id": 8,
                    "name": "subTask3",
                    "description": "This is SubTask 3",
                    "status": "DONE",
                    "startTime": "2022-07-12T15:40:00",
                    "duration": 20,
                    "endTime": "2022-07-12T16:00:00"
                  },
                  {
                    "subTasks": [],
                    "id": 5,
                    "name": "epic3",
                    "description": "This is Epic 3",
                    "status": "NEW"
                  },
                  {
                    "id": 2,
                    "name": "task2",
                    "description": "This is Task 2",
                    "status": "NEW",
                    "startTime": "2022-07-10T05:30:00",
                    "duration": 15,
                    "endTime": "2022-07-10T05:45:00"
                  }
                ]""";
        assertEquals(jsonHistory, sendGetRequest("tasks/history").body());
    }

    @Test
    public void POSTAddTask() {
        Task task7 = new Task("task7", "This is Task 7", IN_PROGRESS,
                LocalDateTime.of(2022, 7, 12, 23, 0), Duration.ofMinutes(30));
        sendPostRequest("task", task7);
        String jsonTask7 = """
                {
                  "id": 1,
                  "name": "task7",
                  "description": "This is Task 7",
                  "status": "IN_PROGRESS",
                  "startTime": "2022-07-12T23:00:00",
                  "duration": 30,
                  "endTime": "2022-07-12T23:30:00"
                }""";
        assertEquals(jsonTask7, sendGetRequest("tasks/task/?id=1").body());
    }

    @Test
    public void POSTAddEpic() {
        Epic epic5 = new Epic("epic5", "This is Epic 5");
        sendPostRequest("epic", epic5);
        String jsonEpic5 = """
                {
                  "subTasks": [],
                  "id": 1,
                  "name": "epic5",
                  "description": "This is Epic 5",
                  "status": "NEW"
                }""";
        assertEquals(jsonEpic5, sendGetRequest("tasks/epic/?id=1").body());
    }

    @Test
    public void POSTAddSubTask() {
        addEpicsAndSubTasks();
        SubTask subTask4 = new SubTask("subTask3", "This is SubTask 3", DONE, 2,
                LocalDateTime.of(2022, 7, 14, 18, 0), Duration.ofMinutes(20));
        sendPostRequest("subtask", subTask4);
        String jsonSubTask4 = """
                {
                  "epicId": 2,
                  "id": 7,
                  "name": "subTask3",
                  "description": "This is SubTask 3",
                  "status": "DONE",
                  "startTime": "2022-07-14T18:00:00",
                  "duration": 20,
                  "endTime": "2022-07-14T18:20:00"
                }""";
        assertEquals(jsonSubTask4, sendGetRequest("tasks/subtask/?id=7").body());
    }

    @Test
    public void POSTUpdateTask() {
        addTasks();
        Task updateTask2 = new Task("task2", "This is updated Task 2 (Method POST)", DONE,
                LocalDateTime.of(2022, 7, 14, 10, 0), Duration.ofMinutes(5));
        sendPostRequest("task/?id=2", updateTask2);
        String jsonTask2 = """
                {
                  "id": 2,
                  "name": "task2",
                  "description": "This is updated Task 2 (Method POST)",
                  "status": "DONE",
                  "startTime": "2022-07-14T10:00:00",
                  "duration": 5,
                  "endTime": "2022-07-14T10:05:00"
                }""";
        assertEquals(jsonTask2, sendGetRequest("tasks/task/?id=2").body());
    }

    @Test
    public void POSTUpdateEpic() {
        addEpicsAndSubTasks();
        Epic updateEpic3 = new Epic("epic3", "This is updated Epic3 2 (Method POST)");
        sendPostRequest("epic/?id=3", updateEpic3);
        String jsonEpic3 = """
                {
                  "subTasks": [],
                  "id": 3,
                  "name": "epic3",
                  "description": "This is updated Epic3 2 (Method POST)",
                  "status": "NEW"
                }""";
        assertEquals(jsonEpic3, sendGetRequest("tasks/epic/?id=3").body());
    }

    @Test
    public void POSTUpdateSubTask() {
        addEpicsAndSubTasks();
        SubTask updateSubtask2 = new SubTask("subTask2", "This is updated SubTask 2 (Method POST)",
                DONE, 1, LocalDateTime.of(2022, 7, 15, 3, 15),
                Duration.ofMinutes(35));
        sendPostRequest("subtask/?id=5", updateSubtask2);
        String jsonSubTask2 = """
                {
                  "epicId": 1,
                  "id": 5,
                  "name": "subTask2",
                  "description": "This is updated SubTask 2 (Method POST)",
                  "status": "DONE",
                  "startTime": "2022-07-15T03:15:00",
                  "duration": 35,
                  "endTime": "2022-07-15T03:50:00"
                }""";
        assertEquals(jsonSubTask2, sendGetRequest("tasks/subtask/?id=5").body());
    }

    @Test
    public void DELETETaskById() {
        addTasks();
        sendDeleteRequest("tasks/task/?id=2");
        assertEquals("", sendGetRequest("tasks/task/?id=2").body());
    }

    @Test
    public void DELETEAllTasks() {
        addTasks();
        sendDeleteRequest("tasks/task/");
        assertEquals("[]", sendGetRequest("tasks/task/").body());
    }

    @Test
    public void DELETESubTaskById() {
        addEpicsAndSubTasks();
        sendDeleteRequest("tasks/subtask/?id=6");
        assertEquals("", sendGetRequest("tasks/subtask/?id=6").body());
    }

    @Test
    public void DELETEAllSubTasks() {
        addEpicsAndSubTasks();
        sendDeleteRequest("tasks/subtask/");
        assertEquals("[]", sendGetRequest("tasks/subtask/").body());
    }

    @Test
    public void DELETEEpicById() {
        addEpicsAndSubTasks();
        sendDeleteRequest("tasks/epic/?id=1");
        assertEquals("", sendGetRequest("tasks/epic/?id=1").body());
    }

    @Test
    public void DELETEAllEpics() {
        addEpicsAndSubTasks();
        sendDeleteRequest("tasks/epic/");
        assertEquals("[]", sendGetRequest("tasks/epic/").body());
    }

    private HttpResponse<String> sendGetRequest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении GET-запроса.");
        }
        return response;
    }

    private void sendPostRequest(String path, Task newTask) {
        try {
            URI url = URI.create("http://localhost:8080/tasks/" + path);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(newTask);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(url).POST(body).build(),
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении POST-запроса.");
        }
    }

    private void sendDeleteRequest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка при выполнении DELETE-запроса.");
        }
    }

    private void addTasks() {
        Task task1 = new Task("task1", "This is Task 1", DONE,
                LocalDateTime.of(2022, 7, 1, 9, 0), Duration.ofMinutes(30));
        manager.addTask(task1);
        Task task2 = new Task("task2", "This is Task 2", NEW,
                LocalDateTime.of(2022, 7, 10, 5, 30), Duration.ofMinutes(15));
        manager.addTask(task2);
    }

    private void addEpicsAndSubTasks() {
        Epic epic1 = new Epic("epic1", "This is Epic 1");
        Epic epic2 = new Epic("epic2", "This is Epic 2");
        Epic epic3 = new Epic("epic3", "This is Epic 3");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        SubTask subTask1 = new SubTask("subTask1", "This is SubTask 1", DONE, epic1.getId(),
                LocalDateTime.of(2022, 7, 1, 2, 0), Duration.ofMinutes(60));
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "This is SubTask 2", NEW, epic1.getId(),
                LocalDateTime.of(2022, 7, 2, 4, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("subTask3", "This is SubTask 3", DONE, epic2.getId(),
                LocalDateTime.of(2022, 7, 12, 15, 40), Duration.ofMinutes(20));
        manager.addSubTask(subTask3);
    }
}