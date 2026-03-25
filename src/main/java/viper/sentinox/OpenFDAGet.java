package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class OpenFDAGet {
    public static JsonObject getAllInfo(String openFDAToken) throws IOException {
        JsonObject object = OpenFDAConnect.connect(openFDAToken);

        return object.getAsJsonArray("results").get(0).getAsJsonObject();
    }

    public static JsonObject getPatient(String openFDAToken) throws IOException {
        return OpenFDAGet
                .getAllInfo(openFDAToken)
                .getAsJsonObject("patient");
    }

    public static JsonArray getPatientReactions(String openFDAToken) throws IOException {
        return OpenFDAGet
                .getPatient(openFDAToken)
                .getAsJsonArray("reaction");

    }

    public static JsonArray getPatientDrugs(String openFDAToken) throws IOException {
        return OpenFDAGet
                .getPatient(openFDAToken)
                .getAsJsonArray("drug");
    }
}
