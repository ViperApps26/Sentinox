package viper.sentinox;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class PubChemPrint {
    private final PubChemGet pubChemGet;

    public PubChemPrint(PubChemGet pubChemGet) {
        this.pubChemGet = pubChemGet;
    }

    public void printAllInfo() throws IOException {
        JsonObject allInfo = pubChemGet.getAllInfo();
        if (allInfo != null) {
            System.out.println(allInfo);
        } else {
            System.out.println("No Results Found");
        }
    }

    public void printReactions() throws IOException {
        ArrayList<String> reactions = pubChemGet.getReactions();

        if (reactions.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String reaction : reactions) {
                System.out.println(reaction);
            }
        }
    }

    public void printMechanism() throws IOException {
        ArrayList<String> mechanisms = pubChemGet.getMechanisms();

        if (mechanisms.isEmpty()) {
            System.out.println("No Results Found");
        } else {
            for (String mechanism : mechanisms) {
                System.out.println(mechanism);
            }
        }
    }
}