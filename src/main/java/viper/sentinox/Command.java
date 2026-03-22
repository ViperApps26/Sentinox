package viper.sentinox;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Command {
    public static void processCommand(String command, List<String> tokens) throws IOException {
        String openFDAToken = tokens.get(0);
        String blueskyToken = BlueskyGetToken.getToken(tokens.get(1));

        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "openfda_set_medicine" -> OpenFDAConnect.setMedicine(parts[1]);
            case "openfda_set_limit" -> OpenFDAConnect.setLimit(Integer.parseInt(parts[1]));
            case "openfda_set_start" -> OpenFDAConnect.setStartDate(parts[1]);
            case "openfda_set_end" -> OpenFDAConnect.setFinalDate(parts[1]);
            case "openfda_print_all" -> OpenFDAPrint.printAllInfo(openFDAToken);
            case "openfda_print_reactions" -> OpenFDAPrint.printReactions(openFDAToken);

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
                OPENFDA
                openfda_print_all: Show all the information about a medicine
                openfda_print_reactions: Show the reactions of a medicine
                openfda_set_medicine: Set a medicine | medicine
                openfda_set_limit: Set a limit of results | number
                openfda_set_start: Set a start date to search | date("YYYYMMDD")
                openfda_set_end: Set an end date to search | date("YYYYMMDD")
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
