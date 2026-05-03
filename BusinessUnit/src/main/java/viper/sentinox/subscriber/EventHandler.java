package viper.sentinox.subscriber;

public interface EventHandler {
    void handle(String topicName, String json);
}