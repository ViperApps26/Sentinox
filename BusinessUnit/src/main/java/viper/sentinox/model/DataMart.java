package viper.sentinox.model;

import java.time.Instant;

public interface DataMart {
    void registerBlueskyEvent(String medicine, String author, String text, String sentiment, Instant date);
    void registerPubChemEvent(String medicine, String reaction);
}