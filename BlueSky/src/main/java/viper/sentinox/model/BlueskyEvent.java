package viper.sentinox.model;

public record BlueskyEvent(long ts, String ss, String medicine, String author, String text, String sentiment,
                           String createdAt) {

}