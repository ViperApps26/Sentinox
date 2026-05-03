package viper.sentinox.control.feeder;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public interface PubChemGetEvents {
    JsonObject getAllInfo() throws IOException;
    ArrayList<String> getReactions() throws IOException;
    ArrayList<String> getMechanisms() throws IOException;
}
