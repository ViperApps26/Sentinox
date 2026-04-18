package viper.sentinox.model;

import java.io.IOException;

public interface BlueskyGetTokenInterface {
    String getAccessToken(String token, String password) throws IOException, InterruptedException;
}
