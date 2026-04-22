package viper.sentinox.control.feeder;

import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.util.List;

public interface BlueskyFeederInteface {
    List<BlueskyEvent> get(String[] medicines,
                           String token,
                           String password)
            throws IOException, InterruptedException;
}
