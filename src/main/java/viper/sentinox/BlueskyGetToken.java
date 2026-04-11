package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlueskyGetToken {
    private static final String BLUESKY_PDS = "https://bsky.social/xrpc/com.atproto.server.createSession";
    private static final HttpClient CLIENT = HttpClient.newBuilder().build();

    public static String getAccessToken(String token, String password) throws IOException, InterruptedException {
        JsonObject newToken = refreshAccessToken(token, password);
        Path filePath = Path.of("BlueskyToken.txt");

        Files.writeString(filePath, newToken.get("refreshJwt").getAsString(), StandardCharsets.UTF_8);
        return newToken.get("accessJwt").getAsString();
    }

    private static JsonObject refreshAccessToken(String refreshToken, String password) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://bsky.social/xrpc/com.atproto.server.refreshSession"))
                .header("Authorization", "Bearer " + refreshToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 400) {
            return refreshAccessToken(getRefreshToken(password), password);
        } else {
            return new Gson().fromJson(response.body(), JsonObject.class);
        }
    }

    public static String getRefreshToken(String password) throws IOException, InterruptedException {
        HttpResponse<String> response = login(password);

        if (response.statusCode() == 200) {
            JsonObject newToken = new Gson().fromJson(response.body(), JsonObject.class);

            return newToken.get("refreshJwt").getAsString();
        }
        throw new RuntimeException("Error in authentication: " + response.body());
    }

    public static HttpResponse<String> login(String password) throws IOException, InterruptedException {
        String jsonBody = String.format(
                "{\"identifier\":\"%s\", \"password\":\"%s\"}",
                "vicraft.bsky.social", password
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BLUESKY_PDS))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}