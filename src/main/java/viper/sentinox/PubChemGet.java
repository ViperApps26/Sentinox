package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class PubChemGet {
    public static JsonObject getAllInfo() throws IOException {
        return PubChemConnect.connect().getAsJsonObject("Record");
    }

    public static ArrayList<String> getReactions() throws IOException {
        ArrayList<String> reactions = new ArrayList<>();

        JsonObject adverseReactions = getSection();

        JsonArray reactionsInfo = adverseReactions.getAsJsonArray("Information");
        for (JsonElement reactionInfo : reactionsInfo) {
            JsonArray reactionsDetail = reactionInfo
                    .getAsJsonObject()
                    .getAsJsonObject("Value")
                    .getAsJsonArray("StringWithMarkup");

            for (JsonElement reactionDetail : reactionsDetail) {
                String reaction = reactionDetail
                        .getAsJsonObject()
                        .get("String")
                        .getAsString();
                reactions.add(reaction);
            }
        }
        return reactions;
    }

    private static JsonObject getSection() throws IOException {
        JsonArray sections = getAllInfo().getAsJsonArray("Section");

        return recursiveSearch(sections);
    }

    private static JsonObject recursiveSearch(JsonArray sections) {
        for (JsonElement element : sections) {
            JsonObject section = element.getAsJsonObject();

            String title = section.has("TOCHeading")
                    ? section.get("TOCHeading").getAsString()
                    : "";

            if (title.equalsIgnoreCase("Adverse Effects")) {
                return section;
            }

            if (section.has("Section")) {
                JsonObject foundSection = recursiveSearch(
                        section.getAsJsonArray("Section")
                );

                if (foundSection != null) {
                    return foundSection;
                }
            }
        }
        return null;
    }

}
