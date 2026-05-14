package viper.sentinox.control.datamart;

public interface EventHandler {
    void handleEvent(String json, String topicName);
}