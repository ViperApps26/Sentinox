package viper.sentinox.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class BlueskyGetToken implements BlueskyGetAccessToken {

    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String user;
    private final String password;

    private String accessToken;
    private String refreshToken;
    private Instant accessExpiration;

    public BlueskyGetToken(String refreshToken, String user, String password) throws IOException, InterruptedException {
        this.refreshToken = refreshToken;
        this.user = user;
        this.password = password;

        checkAccessToken();
    }


    public String getAccessToken() throws IOException, InterruptedException {
        checkAccessToken();
        return accessToken;
    }

    private void checkAccessToken() throws IOException, InterruptedException {
        if (accessToken == null || tokenExpired()) {
            refreshOrLogin();
        }
    }

    private boolean tokenExpired() {
        return accessExpiration == null || Instant.now().isAfter(accessExpiration);
    }

    private void refreshOrLogin() throws IOException, InterruptedException {
        if (!refreshAccessToken()) login();
    }


    private boolean refreshAccessToken() throws IOException, InterruptedException {
        HttpResponse<String> response = getRefreshResponse();

        if (response.statusCode() == 200) {
            setNewTokens(response);
            return true;
        }
        return false;
    }

    private HttpResponse<String> getRefreshResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://bsky.social/xrpc/com.atproto.server.refreshSession"))
                .header("Authorization", "Bearer " + refreshToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    private void login() throws IOException, InterruptedException {
        HttpResponse<String> response = getCreationResponse();

        if (response.statusCode() != 200) {
            throw new RuntimeException("Login failed: " + response.body());
        }
        setNewTokens(response);
    }

    private HttpResponse<String> getCreationResponse() throws IOException, InterruptedException {
        String jsonBody = String.format(
                "{\"identifier\":\"%s\", \"password\":\"%s\"}",
                user,
                password
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://bsky.social/xrpc/com.atproto.server.createSession"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void setNewTokens(HttpResponse<String> response) {
        JsonObject json = gson.fromJson(response.body(), JsonObject.class);

        this.accessToken = json.get("accessJwt").getAsString();
        this.refreshToken = json.get("refreshJwt").getAsString();
        this.accessExpiration = Instant.now().plusSeconds(3600);
    }
}