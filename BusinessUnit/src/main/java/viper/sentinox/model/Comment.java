package viper.sentinox.model;

import viper.sentinox.control.LanguageClassifier;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Comment {

    private static final LanguageClassifier classifier = new LanguageClassifier();

    private final String author;
    private final String text;
    private final String sentiment;
    private final Instant date;
    private final String language;

    public Comment(String author, String text, String sentiment, Instant date) {
        this.author = author;
        this.text = text;
        this.sentiment = sentiment;
        this.date = date;
        this.language = classifier.detectLanguage(text);
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

    public String getLanguage() {
        return language;
    }
}
