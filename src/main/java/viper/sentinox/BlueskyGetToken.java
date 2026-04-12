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
    public static String getToken(String token) throws IOException {
        JsonObject newToken = refresh(token);
        Path filePath = Path.of("BlueskyToken.txt");

        Files.writeString(filePath, newToken.get("refreshJwt").getAsString(), StandardCharsets.UTF_8);
        return newToken.get("accessJwt").getAsString();
    }

    private static JsonObject refresh(String refreshToken) throws IOException {
        Connection.Response response = Jsoup.connect("https://bsky.social/xrpc/com.atproto.server.refreshSession")
                .ignoreContentType(true)
                .header("Authorization", "Bearer " + refreshToken)
                .method(Connection.Method.POST)
                .execute();

        return new Gson().fromJson(response.body(), JsonObject.class);
    }
}