package viper.sentinox.model;

public interface DataMart {
    void registerBlueskyEvent(String medicine, String comment, String sentiment);
    void registerPubChemEvent(String medicine, String reaction);
}