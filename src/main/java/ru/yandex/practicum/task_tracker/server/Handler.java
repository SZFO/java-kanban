package ru.yandex.practicum.task_tracker.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.task_tracker.exceptions.HttpException;
import ru.yandex.practicum.task_tracker.managers.TaskManager;
import ru.yandex.practicum.task_tracker.tasks.Epic;
import ru.yandex.practicum.task_tracker.tasks.SubTask;
import ru.yandex.practicum.task_tracker.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public Handler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String response = "";
        int statusCode = 200;
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET" -> response = getResponse(exchange);
                case "POST" -> postRequest(exchange);
                case "DELETE" -> deleteRequest(exchange);
            }
        } catch (Exception e) {
            statusCode = 400;
            response = gson.toJson(e);
        } finally {
            finalResponse(exchange, response, statusCode);
        }
    }

    private void finalResponse(HttpExchange exchange, String response, int statusCode) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(statusCode, 0);
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new HttpException("Ошибка получения ответа.");
        }
    }

    private String getResponse(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        String query = exchange.getRequestURI().getQuery();
        int id;
        String response = "";
        if (pathSplit.length == 2) {
            return gson.toJson(taskManager.getPrioritizedTasks());
        }
        if (pathSplit.length == 4 && pathSplit[3].equals("epic")) {
            id = getIdFromQuery(uri);
            return gson.toJson(taskManager.getSubTasksInEpic(id));
        }
        if (query == null) {
            switch (pathSplit[2]) {
                case "task":
                    return gson.toJson(taskManager.getAllTasks());
                case "epic":
                    return gson.toJson(taskManager.getAllEpics());
                case "subtask":
                    return gson.toJson(taskManager.getAllSubTasks());
                case "history":
                    return gson.toJson(taskManager.getHistory());
            }
        }
        id = getIdFromQuery(uri);
        if (id != 0) {
            switch (pathSplit[2]) {
                case "task" -> {
                    Task task = taskManager.getTask(id);
                    response = gson.toJson(task);
                }
                case "epic" -> {
                    Epic epic = taskManager.getEpic(id);
                    response = gson.toJson(epic);
                }
                case "subtask" -> {
                    SubTask subTask = taskManager.getSubTask(id);
                    response = gson.toJson(subTask);
                }
            }
        }
        return response;
    }

    private void deleteRequest(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        int id = getIdFromQuery(uri);
        if (id > 0) {
            switch (pathSplit[2]) {
                case "task" -> taskManager.deleteTask(id);
                case "epic" -> taskManager.deleteEpic(id);
                case "subtask" -> taskManager.deleteSubTask(id);
            }
        } else {
            switch (pathSplit[2]) {
                case "task" -> taskManager.deleteAllTasks();
                case "epic" -> taskManager.deleteAllEpics();
                case "subtask" -> taskManager.deleteAllSubTasks();
            }
        }
    }

    private void postRequest(HttpExchange exchange) throws HttpException, IOException {
        URI uri = exchange.getRequestURI();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        int id = getIdFromQuery(uri);
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        switch (pathSplit[2]) {
            case "task" -> {
                Task task = gson.fromJson(body, Task.class);
                if (id > 0) {
                    taskManager.updateTask(task);
                    break;
                }
                taskManager.addTask(task);
            }
            case "epic" -> {
                Epic epic = gson.fromJson(body, Epic.class);
                if (id > 0) {
                    taskManager.updateEpic(epic);
                    break;
                }
                taskManager.addEpic(epic);
            }
            case "subtask" -> {
                SubTask subTask = gson.fromJson(body, SubTask.class);
                if (id > 0) {
                    taskManager.updateSubTask(subTask);
                    break;
                }
                taskManager.addSubTask(subTask);
            }
        }
        inputStream.close();
    }

    private Integer getIdFromQuery(URI uri) {
        if (uri.getQuery() != null) {
            String[] split = uri.getQuery().split("&");
            for (String s1 : split) {
                String name = s1.split("=")[0];
                String value = s1.split("=")[1];
                if (name.equals("id")) {
                    return Integer.parseInt(value);
                }
            }
        }
        return -1;
    }
}