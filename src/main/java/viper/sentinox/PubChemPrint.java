package viper.sentinox;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class PubChemPrint {
    public static void printAllInfo() throws IOException {
        JsonObject allInfo = PubChemGet.getAllInfo();
        if (allInfo != null) {
            System.out.println(allInfo);
        } else {
            System.out.println("No Results Found");
        }
    }

    public static void printReactions() throws IOException {
        ArrayList<String> reactions = PubChemGet.getReactions();

        if (reactions.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String reaction : reactions) {
                System.out.println(reaction);
            }
        }
    }

    public static void printMechanism() throws IOException {
        ArrayList<String> mechanisms = PubChemGet.getMechanisms();

        if (mechanisms.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String mechanism : mechanisms) {
                System.out.println(mechanism);
            }
        }
    }
}
