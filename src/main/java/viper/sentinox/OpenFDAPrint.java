package viper.sentinox;

import java.io.IOException;

public class OpenFDAPrint {
    public static void printAllInfo(String openFDAToken) throws IOException {
        System.out.println(OpenFDAGet.getAllInfo(openFDAToken));
    }

    public static void printPatient(String openFDAToken) throws IOException {
        System.out.println(OpenFDAGet.getPatient(openFDAToken));
    }

    public static void printPatientReactions(String openFDAToken) throws IOException {
        for (var reaction : OpenFDAGet.getPatientReactions(openFDAToken)) {
            System.out.println(reaction.getAsJsonObject().get("reactionmeddrapt").getAsString());
        }
    }

    public static void printPatientDrugs(String openFDAToken) throws IOException {
        for (var drugs : OpenFDAGet.getPatientDrugs(openFDAToken)) {
            System.out.println(drugs.getAsJsonObject().get("medicinalproduct").getAsString() + " "+ drugs.getAsJsonObject().get("drugcharacterization").getAsString());
        }
    }
}
