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

    private final String blueskyPds;
    private final String refreshSessionUrl;
    private final String identifier;
    private final Path filePath;
    private final HttpClient client;
    private final Gson gson;

    public BlueskyGetToken() {
        this(
                "https://bsky.social/xrpc/com.atproto.server.createSession",
                "https://bsky.social/xrpc/com.atproto.server.refreshSession",
                "vicraft.bsky.social",
                Path.of("BlueskyToken.txt"),
                HttpClient.newBuilder().build()
        );
    }

    public BlueskyGetToken(String blueskyPds, String refreshSessionUrl, String identifier, Path filePath, HttpClient client) {
        this.blueskyPds = blueskyPds;
        this.refreshSessionUrl = refreshSessionUrl;
        this.identifier = identifier;
        this.filePath = filePath;
        this.client = client;
        this.gson = new Gson();
    }

    public String getAccessToken(String token, String password) throws IOException, InterruptedException {
        JsonObject newToken = refreshAccessToken(token, password);
        saveRefreshToken(newToken);
        return newToken.get("accessJwt").getAsString();
    }

    private JsonObject refreshAccessToken(String refreshToken, String password) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(refreshSessionUrl))
                .header("Authorization", "Bearer " + refreshToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 400) {
            String newRefreshToken = getRefreshToken(password);
            return refreshAccessToken(newRefreshToken, password);
        }

        return gson.fromJson(response.body(), JsonObject.class);
    }

    public String getRefreshToken(String password) throws IOException, InterruptedException {
        HttpResponse<String> response = login(password);

        if (response.statusCode() == 200) {
            JsonObject newToken = gson.fromJson(response.body(), JsonObject.class);
            return newToken.get("refreshJwt").getAsString();
        }

        throw new RuntimeException("Error in authentication: " + response.body());
    }

    public HttpResponse<String> login(String password) throws IOException, InterruptedException {
        String jsonBody = String.format(
                "{\"identifier\":\"%s\", \"password\":\"%s\"}",
                identifier,
                password
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(blueskyPds))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void saveRefreshToken(JsonObject newToken) throws IOException {
        String refreshJwt = newToken.get("refreshJwt").getAsString();
        Files.writeString(filePath, refreshJwt, StandardCharsets.UTF_8);
    }

    public String getBlueskyPds() {
        return blueskyPds;
    }

    public String getRefreshSessionUrl() {
        return refreshSessionUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Path getFilePath() {
        return filePath;
    }
}
