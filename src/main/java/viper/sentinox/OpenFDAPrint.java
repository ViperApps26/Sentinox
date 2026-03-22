package viper.sentinox;

import com.google.gson.JsonObject;

import java.io.IOException;

public class OpenFDAPrint {
    public static void printAllInfo(String openFDAToken) throws IOException {
        JsonObject object = OpenFDAConnect.connect(openFDAToken);

        System.out.println("First event:");
        System.out.println(object.getAsJsonArray("results").get(0));
    }

    public static void printReactions(String openFDAToken) throws IOException {
        JsonObject object = OpenFDAConnect.connect(openFDAToken);

        JsonObject firstEvent = object
                .getAsJsonArray("results")
                .get(0)
                .getAsJsonObject();
        JsonObject patient = firstEvent.getAsJsonObject("patient");

        for (var reactionElement : patient.getAsJsonArray("reaction")) {
            JsonObject reaction = reactionElement.getAsJsonObject();

            System.out.println(reaction.get("reactionmeddrapt").getAsString());
        }
    }
}
