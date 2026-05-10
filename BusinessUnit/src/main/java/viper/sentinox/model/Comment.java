package viper.sentinox.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Comment {

    private final String author;
    private final String text;
    private final String sentiment;
    private final Instant date;

    public Comment(String author, String text, String sentiment, Instant date) {
        this.author = author;
        this.text = text;
        this.sentiment = sentiment;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                .withZone(ZoneId.systemDefault());

        return formatter.format(date);
    }
}
