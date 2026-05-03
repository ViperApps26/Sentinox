package viper.sentinox.control.store;

import viper.sentinox.model.BlueskyEvent;


public interface BlueskySaveEvents {
    void save(BlueskyEvent event);
}
