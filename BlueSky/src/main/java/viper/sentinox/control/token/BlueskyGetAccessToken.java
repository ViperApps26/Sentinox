package viper.sentinox.control.token;

import java.io.IOException;

public interface BlueskyGetAccessToken {
    String getAccessToken() throws IOException, InterruptedException;
}
