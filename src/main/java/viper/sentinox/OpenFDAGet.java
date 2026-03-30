package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class OpenFDAGet {
    public static JsonArray getAllInfo() throws IOException {
        return OpenFDAConnect.connect().getAsJsonArray("data");
    }

    public static ArrayList<String> getIds() throws IOException {
        JsonArray relatedMedicines = getAllInfo();
        ArrayList<String> ids = new ArrayList<>();

        for (int i = 0; i < relatedMedicines.size(); i++) {
            ids.add(relatedMedicines.get(i).getAsJsonObject().get("setid").getAsString());
        }
        return ids;
    }

    public static JsonObject getIdInfo() throws IOException {
        return OpenFDAConnect.connectId();
    }

    public static ArrayList<String> getReactions() throws IOException {
        ArrayList<String> reactions = new ArrayList<>();

        JsonArray items = OpenFDAConnect.connectId()
                .getAsJsonObject("document")
                .getAsJsonObject("component")
                .getAsJsonObject("structuredBody")
                .getAsJsonArray("component")
                .get(4).getAsJsonObject()
                .getAsJsonObject("section")
                .getAsJsonObject("text")
                .getAsJsonObject("list")
                .getAsJsonArray("item");


            for (var item : items) {
                reactions.add(item
                        .getAsJsonObject()
                        .get("content")
                        .getAsString()
                );
            }

        return reactions;
    }
}
