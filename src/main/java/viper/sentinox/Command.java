package viper.sentinox;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Command {
    public static void processCommand(String command, String token, String password, String databaseURL) throws IOException, InterruptedException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "update_medicines_data" -> {
                String blueskyToken = BlueskyGetToken.getAccessToken(token, password);
                DatabaseInsert.updateMedicinesData(blueskyToken, databaseURL);
            }

            case "pubchem_set_medicine" -> {
                if (validate(parts, 2, 2,-1)) {
                    PubChemConnect.setMedicine(parts[1]);
                }
            }

            case "pubchem_print_all" -> PubChemPrint.printAllInfo();
            case "pubchem_print_reactions" -> PubChemPrint.printReactions();
            case "pubchem_print_mechanisms" -> PubChemPrint.printMechanism();

            case "pubchem_save_reactions" -> DatabaseInsert.saveReactions(databaseURL);
            case "pubchem_save_medicines" -> {
                if (validate(parts, 2, 100,-1)) {
                    String[] medicines = Arrays.copyOfRange(parts, 1, parts.length);
                    DatabaseInsert.saveMedicinesFromList(medicines, databaseURL);
                }
            }

            case "pubchem_show_medicines" -> DataViewer.showMedicines(databaseURL);
            case "pubchem_show_reactions" -> DataViewer.showPubChemReactions(databaseURL);
            case "pubchem_show_summary" -> DataViewer.showMedicineSummary(databaseURL);

            case "bluesky_set_query" -> {
                if (validate(parts, 2, 2,-1)) {
                    BlueskyConnect.setQuery(parts[1]);
                }
            }
            case "bluesky_set_limit" -> {
                if (validate(parts, 2, 2,-1)) {
                    BlueskyConnect.setLimit(Integer.parseInt(parts[1]));
                }
            }
            case "bluesky_set_start" -> {
                if (validate(parts, 2, 2,-1)) {
                    BlueskyConnect.setStartDate(parts[1]);
                }
            }
            case "bluesky_set_end" -> {
                if (validate(parts, 2, 2,-1)) {
                    BlueskyConnect.setFinalDate(parts[1]);
                }
            }

            case "bluesky_print_posts" -> {
                String blueskyToken = BlueskyGetToken.getAccessToken(token, password);
                BlueskyPrint.printPosts(blueskyToken);
            }

            case "bluesky_save_posts" -> {
                String blueskyToken = BlueskyGetToken.getAccessToken(token, password);
                DatabaseInsert.saveBlueskyPosts(blueskyToken, databaseURL);
            }

            case "bluesky_show_db" -> DataViewer.showBlueskyPosts(databaseURL);

            case "help" -> help();
            default -> System.out.println("Command not found");
        }
    }

    public static boolean validate(String[] parts, int minLength, int maxLength, int positionToCheck) {
        if (parts.length < minLength | parts.length > maxLength) {
            System.out.println("Incorrect command length");
            return false;
        } else if (positionToCheck >= 0 && !parts[positionToCheck].matches("\\d+")) {
            System.out.println("Incorrect attribute type");
            return false;
        }

        return true;
    }

    public static String askCommand(Scanner scanner) {
        System.out.println("Write a command\n(Write \"help\" if you don't know any command)");
        return scanner.nextLine().trim();
    }

    public static void help() {
        System.out.println("""
            - HELP - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            command: description | params
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            GENERAL
            exit: Exit the program
            help: Show this help menu
            
            update_medicines_data: Insert the current content for each medicine
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            PUBCHEM
            pubchem_set_medicine: Set a medicine | medicine
            
            pubchem_print_all: Show all the information about a medicine
            pubchem_print_reactions: Show all the reactions about a medicine
            pubchem_print_mechanisms: Show all the mechanisms of action of a medicine

            pubchem_save_reactions: Save the current medicine and its reactions into the database
            pubchem_save_medicines: Add multiple medicines to the database | medicine1 medicine2 ...
            
            pubchem_show_medicines: Show all stored medicines
            pubchem_show_reactions: Show all stored PubChem reactions
            pubchem_show_summary: Show a summary of all stored medicines and reactions
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            BLUESKY
            bluesky_set_query: Set a search message | search
            bluesky_set_limit: Set a limit of results | number
            bluesky_set_start: Set a start date to search | date("YYYY-MM-DD")
            bluesky_set_end: Set an end date to search | date("YYYY-MM-DD")

            bluesky_print_posts: Show all messages from posts
            
            bluesky_save_posts: Save posts from Bluesky into the database
            
            bluesky_show_db: Show all stored Bluesky posts
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            """);
    }

}


