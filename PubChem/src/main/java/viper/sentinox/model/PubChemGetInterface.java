package viper.sentinox.model;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public interface PubChemGetInterface {
    JsonObject getAllInfo() throws IOException;
    ArrayList<String> getReactions() throws IOException;
    ArrayList<String> getMechanisms() throws IOException;
}
