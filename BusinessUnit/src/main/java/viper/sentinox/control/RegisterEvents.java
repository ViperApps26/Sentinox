package viper.sentinox.control;

import java.time.Instant;

public interface RegisterEvents {
    void registerBlueskyEvent(String medicine, String author, String text, String sentiment, Instant date);
    void registerPubChemEvent(String medicine, String reaction);
}