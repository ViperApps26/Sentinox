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
        extractElements(getSection("Adverse Effects"), reactions);

        return reactions;
    }

    public static ArrayList<String> getMechanisms() throws IOException {
        ArrayList<String> mechanisms = new ArrayList<>();
        extractElements(getSection("Mechanism of Action"), mechanisms);

        return mechanisms;
    }

    private static void extractElements(JsonObject sectionParameters, ArrayList<String> infoList) {
        JsonArray sectionDetails = sectionParameters.getAsJsonArray("Information");

        for (JsonElement InfoDetails : sectionDetails) {
            JsonArray stringsDetails = InfoDetails
                    .getAsJsonObject()
                    .getAsJsonObject("Value")
                    .getAsJsonArray("StringWithMarkup");

            for (JsonElement stringDetails : stringsDetails) {
                String reaction = stringDetails
                        .getAsJsonObject()
                        .get("String")
                        .getAsString();

                infoList.add(reaction);
            }
        }
    }


    private static JsonObject getSection(String search) throws IOException {
        JsonArray sections = getAllInfo().getAsJsonArray("Section");

        return recursiveSearch(sections, search);
    }

    private static JsonObject recursiveSearch(JsonArray sections, String search) {
        for (JsonElement element : sections) {
            JsonObject section = element.getAsJsonObject();

            JsonObject foundSection = verifyTitle(search, section);
            if (foundSection != null) return foundSection;

            if (section.has("Section")) {
                foundSection = recursiveSearch(
                        section.getAsJsonArray("Section"),
                        search
                );

                if (foundSection != null) {
                    return foundSection;
                }
            }
        }
        return null;
    }

    private static JsonObject verifyTitle(String search, JsonObject section) {
        String title = section.has("TOCHeading")
                ? section.get("TOCHeading").getAsString()
                : "";

        if (title.equalsIgnoreCase(search)) {
            return section;
        }
        return null;
    }

}
