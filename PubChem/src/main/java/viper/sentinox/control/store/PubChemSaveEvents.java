package viper.sentinox.control.store;

import viper.sentinox.model.PubChemEvent;

public interface PubChemSaveEvents {
    void save(PubChemEvent event);
}
