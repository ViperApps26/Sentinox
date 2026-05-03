package viper.sentinox.control.feeder;

import java.io.IOException;
import java.util.ArrayList;

public interface PubChemGetEvents {
    ArrayList<String> getReactions() throws IOException;
}
