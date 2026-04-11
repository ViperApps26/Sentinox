package viper.sentinox;

import java.io.IOException;
import java.util.List;

public class PubChemPrint {

    private final PubChemGet pubChemGet;

    public PubChemPrint(PubChemGet pubChemGet) {
        this.pubChemGet = pubChemGet;
    }

    public void printAllInfo() throws IOException {
        System.out.println(pubChemGet.getAllInfo());
    }

    public void printReactions() throws IOException {
        List<String> reactions = pubChemGet.getReactions();

        for (String reaction : reactions) {
            System.out.println(reaction);
        }
    }

    public void printMechanism() throws IOException {
        List<String> mechanisms = pubChemGet.getMechanisms();

        for (String mechanism : mechanisms) {
            System.out.println(mechanism);
        }
    }
}