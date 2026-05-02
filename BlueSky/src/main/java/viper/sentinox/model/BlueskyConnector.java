package viper.sentinox.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class BlueskyConnector {
    private final String baseUrl;
    private final String datePattern;

    private String query;
    private int limit;
    private String startDate;
    private String finalDate;

    public BlueskyConnector() {
        this.baseUrl = "https://bsky.social/xrpc";
        this.datePattern = "\\d{4}-\\d{2}-\\d{2}";
        this.query = "ibuprofen";
        this.limit = 10;

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime oneMonthAgo = now.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        this.startDate = oneMonthAgo.format(formatter);
        this.finalDate = now.format(formatter);
    }

    public JsonObject connector(String token) throws IOException {
        String path = getBlueskyPath();

        Connection.Response response = request(path, token);
        String body = response.body();

        return new Gson().fromJson(body, JsonObject.class);
    }

    private String getBlueskyPath() {
        String pattern = "/app.bsky.feed.searchPosts?q=%s&limit=%d&since=%s&until=%s";

        return String.format(
                pattern,
                query,
                limit,
                startDate,
                finalDate
        );
    }

    private Connection.Response request(String url, String token) throws IOException {
        return Jsoup.connect(baseUrl + url)
                .ignoreContentType(true)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .method(Connection.Method.GET)
                .execute();
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStartDate(String newDate) {
        if (newDate.matches(datePattern)) {
            this.startDate = newDate + "T00:00:00Z";
        } else {
            System.out.println("Invalid format, use YYYY-MM-DD");
        }
    }

    public void setFinalDate(String newDate) {
        if (newDate.matches(datePattern)) {
            this.finalDate = newDate + "T00:00:00Z";
        } else {
            System.out.println("Invalid format, use YYYY-MM-DD");
        }
    }

    public String getQuery() {
        return query;
    }

    public int getLimit() {
        return limit;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getFinalDate() {
        return finalDate;
    }
}