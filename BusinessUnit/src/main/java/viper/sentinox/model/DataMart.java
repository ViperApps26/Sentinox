package viper.sentinox.model;

public interface DataMart {
    void registerSentiment(String medicine, String sentiment);
    void registerReaction(String medicine, String reaction);
    String getSummary();
}