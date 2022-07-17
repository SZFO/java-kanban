package ru.yandex.practicum.task_tracker.server;

import ru.yandex.practicum.task_tracker.exceptions.HttpException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class KVTaskClient {
    private final HttpClient client;
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.client = HttpClient.newHttpClient();
        this.url = url;
        this.apiToken = register();
    }

    private String register() {
        URI uri = URI.create(url + "register/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return Objects.requireNonNull(request(request)).body();
    }

    public void put(String value, String gson) {
        URI uri = URI.create(url + "save/" + value + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .header("Accept", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        request(request);
    }

    public String load(String value) {
        URI uri = URI.create(url + "load/" + value + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        return Objects.requireNonNull(request(request)).body();
    }

    private HttpResponse<String> request(HttpRequest request) {
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            return client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Ошибка выполнения запроса.");
        }
    }
}