package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlueskyGetToken {

    private final String refreshUrl;
    private final Path tokenFilePath;
    private final Gson gson;

    public BlueskyGetToken() {
        this.refreshUrl = "https://bsky.social/xrpc/com.atproto.server.refreshSession";
        this.tokenFilePath = Path.of("BlueskyToken.txt");
        this.gson = new Gson();
    }

    public String getToken(String token) throws IOException {
        JsonObject newToken = refresh(token);
        saveRefreshToken(newToken);
        return extractAccessToken(newToken);
    }

    private JsonObject refresh(String refreshToken) throws IOException {
        Connection.Response response = Jsoup.connect(refreshUrl)
                .ignoreContentType(true)
                .header("Authorization", "Bearer " + refreshToken)
                .method(Connection.Method.POST)
                .execute();

        return gson.fromJson(response.body(), JsonObject.class);
    }

    private void saveRefreshToken(JsonObject newToken) throws IOException {
        String refreshJwt = newToken.get("refreshJwt").getAsString();
        Files.writeString(tokenFilePath, refreshJwt, StandardCharsets.UTF_8);
    }

    private String extractAccessToken(JsonObject newToken) {
        return newToken.get("accessJwt").getAsString();
    }

    public Path getTokenFilePath() {
        return tokenFilePath;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }
}
