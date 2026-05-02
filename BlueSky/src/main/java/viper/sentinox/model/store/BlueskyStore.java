package viper.sentinox.model.store;

import viper.sentinox.model.BlueskyEvent;

import java.util.List;


public interface BlueskyStore {
    void save(BlueskyEvent event);
}
