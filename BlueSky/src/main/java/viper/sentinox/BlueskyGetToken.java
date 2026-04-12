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
    private final String refreshUrl;
    private final String identifier;
    private final Path filePath;
    private final HttpClient client;
    private final Gson gson;

    public BlueskyGetToken() {
        this.blueskyPds = "https://bsky.social/xrpc/com.atproto.server.createSession";
        this.refreshUrl = "https://bsky.social/xrpc/com.atproto.server.refreshSession";
        this.identifier = "vicraft.bsky.social";
        this.filePath = Path.of("BlueskyToken.txt");
        this.client = HttpClient.newBuilder().build();
        this.gson = new Gson();
    }

    public String getAccessToken(String token, String password) throws IOException, InterruptedException {
        JsonObject newToken = refreshAccessToken(token, password);

        Files.writeString(filePath,
                newToken.get("refreshJwt").getAsString(),
                StandardCharsets.UTF_8
        );

        return newToken.get("accessJwt").getAsString();
    }

    private JsonObject refreshAccessToken(String refreshToken, String password) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(refreshUrl))
                .header("Authorization", "Bearer " + refreshToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 400) {
            return refreshAccessToken(getRefreshToken(password), password);
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

    public Path getFilePath() {
        return filePath;
    }

    public String getIdentifier() {
        return identifier;
    }
}
