package viper.sentinox;

public class BlueskyEvent {

    private final long ts;
    private final String ss;
    private final String medicine;
    private final String author;
    private final String text;
    private final String sentiment;
    private final String createdAt;

    public BlueskyEvent(long ts, String ss, String medicine,
                        String author, String text,
                        String sentiment, String createdAt) {
        this.ts = ts;
        this.ss = ss;
        this.medicine = medicine;
        this.author = author;
        this.text = text;
        this.sentiment = sentiment;
        this.createdAt = createdAt;
    }

    public long getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }

    public String getMedicine() {
        return medicine;
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

    public String getCreatedAt() {
        return createdAt;
    }
}