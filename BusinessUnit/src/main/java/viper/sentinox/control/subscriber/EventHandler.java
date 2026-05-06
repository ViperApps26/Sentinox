package viper.sentinox.control.subscriber;

public interface EventHandler {
    void handle(String topicName, String json);
}