package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class PubChemGet {
    public static JsonObject getAllInfo() throws IOException {
        JsonObject connection = PubChemConnect.connect();

        return connection != null && connection.has("Record")
                ? connection.getAsJsonObject("Record")
                : null;
    }

    public static ArrayList<String> getReactions() throws IOException {
        ArrayList<String> reactions = new ArrayList<>();
        JsonObject section = getSection("Adverse Effects");
        if (section != null) {
            extractElements(section, reactions);
        }
        return reactions;
    }

    public static ArrayList<String> getMechanisms() throws IOException {
        ArrayList<String> mechanisms = new ArrayList<>();
        JsonObject section = getSection("Mechanism of Action");
        if (section != null) {
            extractElements(section, mechanisms);
        }
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
        JsonObject allInfo = getAllInfo();

        return allInfo != null && allInfo.has("Section")
                ? recursiveSearch(allInfo.getAsJsonArray("Section"), search)
                : null;
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
