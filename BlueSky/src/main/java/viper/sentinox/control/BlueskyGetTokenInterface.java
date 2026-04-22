package viper.sentinox.control;

import java.io.IOException;

public interface BlueskyGetTokenInterface {
    String getAccessToken(String token, String password) throws IOException, InterruptedException;
}
