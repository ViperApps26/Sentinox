package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;

public class BlueskyConnect {
    private static final String BASE_URL = "https://bsky.social/xrpc";

    public static String query = "nolotil";
    public static int limit = 10;
    public static String startDate = "2025-01-01T00:00:00Z";
    public static String finalDate = "2025-02-01T00:00:00Z";

    public static JsonObject connect(String token) throws IOException {
        String path = getBlueskyPath();

        Connection.Response response = request(path, token);
        String body = response.body();

        return new Gson().fromJson(body, JsonObject.class);
    }

    private static String getBlueskyPath() {
        String pattern = "/app.bsky.feed.searchPosts?q=%s&limit=%d&since=%s&until=%s";

        return String.format(
                pattern,
                query,
                limit,
                startDate,
                finalDate
        );
    }

    private static Connection.Response request(String url, String token) throws IOException {
        return Jsoup.connect(BASE_URL + url)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .method(Connection.Method.GET)
                .execute();
    }

    public static void setQuery(String query) {
        BlueskyConnect.query = query;
    }

    public static void setLimit(int limit) {
        BlueskyConnect.limit = limit;
    }

    public static void setStartDate(String newDate) {
        if (newDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            BlueskyConnect.startDate = newDate + "T00:00:00Z";
        } else {
            System.out.println("Formato inválido, usa YYYY-MM-DD");
        }
    }

    public static void setFinalDate(String newDate) {
        if (newDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            BlueskyConnect.startDate = newDate + "T00:00:00Z";
        } else {
            System.out.println("Formato inválido, usa YYYY-MM-DD");
        }
    }
}
