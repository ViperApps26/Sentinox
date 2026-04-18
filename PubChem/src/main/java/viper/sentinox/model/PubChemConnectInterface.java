package viper.sentinox.model;

import com.google.gson.JsonObject;

import java.io.IOException;

public interface PubChemConnectInterface {
    JsonObject connect() throws IOException;

    void setMedicine(String medicine);

    String getCID() throws IOException;
    String getMedicine();
}
