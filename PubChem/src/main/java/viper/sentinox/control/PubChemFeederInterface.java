package viper.sentinox.control;

import java.io.IOException;

public interface PubChemFeederInterface {
    void feedReactionsFromList(String[] medicines) throws IOException;
}
