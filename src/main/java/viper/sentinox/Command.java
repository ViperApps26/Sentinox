package viper.sentinox;

import java.io.IOException;
import java.util.Scanner;

public class Command {
    public static void processCommand(String command, String token) throws IOException {
        String blueskyToken = BlueskyGetToken.getToken(token);

        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "pubchem_set_medicine" -> PubChemConnect.setMedicine(parts[1]);
            case "pubchem_print_all" -> PubChemPrint.printAllInfo();
            case "pubchem_print_reactions" -> PubChemPrint.printReactions();

            case "bluesky_set_query" -> BlueskyConnect.setQuery(parts[1]);
            case "bluesky_set_limit" -> BlueskyConnect.setLimit(Integer.parseInt(parts[1]));
            case "bluesky_set_start" -> BlueskyConnect.setStartDate(parts[1]);
            case "bluesky_set_end" -> BlueskyConnect.setFinalDate(parts[1]);
            case "bluesky_print_posts" -> BlueskyPrint.printPosts(blueskyToken, Integer.parseInt(parts[1]));

            case "help" -> help();
            default -> System.out.println("Command not found");
        }
    }

    public static String askCommand(Scanner scanner) {
        System.out.println("Write a command\n" + "(Write \"help\" if you don't know any command)");
        return scanner.nextLine().trim();
    }

    public static void help() {
        System.out.println("""
                - HELP - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                command: description | params
                - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                GENERAL
                exit: Exit the program
                - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                PUBCHEM
                pubchem_print_info: Show all the information about a medicine
                pubchem_set_medicine: Set a medicine | medicine
                - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
                BLUESKY
                bluesky_set_query: Set a search message | search
                bluesky_set_limit: Set a limit of results | number
                bluesky_set_start: Set a start date to search | date("YYYY-MM-DD")
                bluesky_set_end: Set an end date to search | date("YYYY-MM-DD")
                bluesky_print_posts: Show the number of past posts | number
                - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"""
        );
    }
}
