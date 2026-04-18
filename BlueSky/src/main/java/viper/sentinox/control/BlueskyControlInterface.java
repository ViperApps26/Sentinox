package viper.sentinox.control;

import java.io.IOException;

public interface BlueskyControlInterface {
    void execute(String token, String password)
            throws IOException, InterruptedException;
}
