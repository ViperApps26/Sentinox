package viper.sentinox.control;

import java.io.IOException;

public interface BlueskyGetAccessToken {
    String getAccessToken() throws IOException, InterruptedException;
}
