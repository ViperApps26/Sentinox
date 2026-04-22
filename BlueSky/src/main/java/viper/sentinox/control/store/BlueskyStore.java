package viper.sentinox.control.store;

import viper.sentinox.model.BlueskyEvent;


public interface BlueskyStore {
    void save(BlueskyEvent event);
}
