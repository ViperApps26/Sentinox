package viper.sentinox.control.oauth;

import java.io.IOException;

public interface BlueskyGetAccessToken {
    String getAccessToken() throws IOException, InterruptedException;
}
