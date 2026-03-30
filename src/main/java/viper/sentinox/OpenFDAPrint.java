package viper.sentinox;

import java.io.IOException;

public class OpenFDAPrint {
    public static void printAllInfo() throws IOException {
        System.out.println(OpenFDAGet.getAllInfo());
    }

    public static void printIds() throws IOException {
        for (String id : OpenFDAGet.getIds()) {
            System.out.println(id);
        }
    }

    public static void printIdInfo() throws IOException {
        System.out.println(OpenFDAGet.getIdInfo());
    }

    public static void printReactions() throws IOException {
        for (String reaction: OpenFDAGet.getReactions()) {
            System.out.println(reaction);
        }
    }
}
