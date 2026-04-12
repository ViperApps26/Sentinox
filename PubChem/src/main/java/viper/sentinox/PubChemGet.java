package viper.sentinox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PubChemGet {

    private final PubChemConnect pubChemConnect;

    public PubChemGet(PubChemConnect pubChemConnect) {
        this.pubChemConnect = pubChemConnect;
    }

    public JsonObject getAllInfo() throws IOException {
        return pubChemConnect.connect().getAsJsonObject("Record");
    }

    public List<String> getReactions() throws IOException {
        List<String> reactions = new ArrayList<>();
        JsonObject section = getSection("Adverse Effects");

        if (section != null) {
            extractElements(section, reactions);
        }

        return reactions;
    }

    public List<String> getMechanisms() throws IOException {
        List<String> mechanisms = new ArrayList<>();
        JsonObject section = getSection("Mechanism of Action");

        if (section != null) {
            extractElements(section, mechanisms);
        }

        return mechanisms;
    }

    private void extractElements(JsonObject sectionParameters, List<String> infoList) {
        if (sectionParameters == null || !sectionParameters.has("Information")) {
            return;
        }

        JsonArray sectionDetails = sectionParameters.getAsJsonArray("Information");

        for (JsonElement infoDetails : sectionDetails) {
            JsonObject infoObject = infoDetails.getAsJsonObject();

            if (!infoObject.has("Value")) {
                continue;
            }

            JsonObject valueObject = infoObject.getAsJsonObject("Value");

            if (!valueObject.has("StringWithMarkup")) {
                continue;
            }

            JsonArray stringsDetails = valueObject.getAsJsonArray("StringWithMarkup");

            for (JsonElement stringDetails : stringsDetails) {
                JsonObject stringObject = stringDetails.getAsJsonObject();

                if (stringObject.has("String")) {
                    String text = stringObject.get("String").getAsString();
                    infoList.add(text);
                }
            }
        }
    }

    private JsonObject getSection(String search) throws IOException {
        JsonObject allInfo = getAllInfo();

        if (allInfo == null || !allInfo.has("Section")) {
            return null;
        }

        JsonArray sections = allInfo.getAsJsonArray("Section");
        return recursiveSearch(sections, search);
    }

    private JsonObject recursiveSearch(JsonArray sections, String search) {
        for (JsonElement element : sections) {
            JsonObject section = element.getAsJsonObject();

            JsonObject foundSection = verifyTitle(search, section);
            if (foundSection != null) {
                return foundSection;
            }

            if (section.has("Section")) {
                foundSection = recursiveSearch(section.getAsJsonArray("Section"), search);

                if (foundSection != null) {
                    return foundSection;
                }
            }
        }

        return null;
    }

    private JsonObject verifyTitle(String search, JsonObject section) {
        String title = section.has("TOCHeading")
                ? section.get("TOCHeading").getAsString()
                : "";

        if (title.equalsIgnoreCase(search)) {
            return section;
        }

        return null;
    }
}//test//test