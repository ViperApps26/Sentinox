package viper.sentinox.control;

import java.io.IOException;

public interface BlueskyPublisherInterface {
    void publishPosts(String token) throws IOException;
}
