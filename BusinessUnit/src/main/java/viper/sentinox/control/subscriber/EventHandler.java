package viper.sentinox.control.subscriber;

public interface EventHandler {
    void handleEvent(String json, String topicName);
}