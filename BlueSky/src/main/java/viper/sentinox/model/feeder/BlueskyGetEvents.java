package viper.sentinox.model.feeder;

import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.util.List;

public interface BlueskyGetEvents {
    List<BlueskyEvent> get(String medicine) throws IOException, InterruptedException;
}
