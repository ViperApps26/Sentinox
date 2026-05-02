package viper.sentinox.model;

public interface Sentiment {
    SentimentResult analyze(String text);
}
