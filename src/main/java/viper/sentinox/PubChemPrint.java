package viper.sentinox;

import java.io.IOException;

public class PubChemPrint {
    public static void printAllInfo() throws IOException {
        System.out.println(PubChemGet.getAllInfo());
    }

    public static void printReactions() throws IOException {
        for (String reaction: PubChemGet.getReactions()) {
            System.out.println(reaction);
        }
    }

    public static void printMechanism() throws IOException {
        for (String mechanism: PubChemGet.getMechanisms()) {
            System.out.println(mechanism);
        }
    }
}
