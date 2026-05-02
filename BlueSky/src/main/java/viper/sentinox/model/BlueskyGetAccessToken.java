package viper.sentinox.model;

import java.io.IOException;

public interface BlueskyGetAccessToken {
    String getAccessToken() throws IOException, InterruptedException;
}
