package viper.sentinox.control.DataMartFeader;

public interface EventHandler {
    void handleEvent(String json, String topicName);
}