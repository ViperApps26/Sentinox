package viper.sentinox;

import java.io.IOException;
import java.util.Scanner;

public class Command {
    public static void processCommand(String command, String token) throws IOException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "pubchem_set_medicine" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: pubchem_set_medicine <medicine>");
                } else {
                    PubChemConnect.setMedicine(parts[1]);
                }
            }

            case "pubchem_print_all" -> PubChemPrint.printAllInfo();
            case "pubchem_print_reactions" -> PubChemPrint.printReactions();
            case "pubchem_print_mechanisms" -> PubChemPrint.printMechanism();
            case "pubchem_save_reactions" -> DataRepository.savePubChemMedicineAndReactions();
            case "pubchem_save_initial" -> DataRepository.saveInitialMedicines(token);

            case "pubchem_add_medicines" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: pubchem_add_medicines <medicine1> <medicine2> ...");
                } else {
                    String[] medicines = new String[parts.length - 1];
                    System.arraycopy(parts, 1, medicines, 0, parts.length - 1);
                    DataRepository.saveMedicinesFromList(medicines, token);
                }
            }

            case "pubchem_load_file" -> DataRepository.saveMedicinesFromFile(token);
            case "pubchem_show_medicines" -> DataViewer.showMedicines();
            case "pubchem_show_db" -> DataViewer.showPubChemReactions();
            case "pubchem_show_summary" -> DataViewer.showMedicineSummary();

            case "bluesky_set_query" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: bluesky_set_query <query>");
                } else {
                    BlueskyConnect.setQuery(parts[1]);
                }
            }

            case "bluesky_set_limit" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: bluesky_set_limit <number>");
                } else {
                    BlueskyConnect.setLimit(Integer.parseInt(parts[1]));
                }
            }

            case "bluesky_set_start" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: bluesky_set_start <YYYY-MM-DD>");
                } else {
                    BlueskyConnect.setStartDate(parts[1]);
                }
            }

            case "bluesky_set_end" -> {
                if (parts.length < 2) {
                    System.out.println("Usage: bluesky_set_end <YYYY-MM-DD>");
                } else {
                    BlueskyConnect.setFinalDate(parts[1]);
                }
            }

            case "bluesky_print_posts" -> {
                if (token.isBlank()) {
                    System.out.println("Bluesky token not found.");
                } else if (parts.length < 2) {
                    System.out.println("Usage: bluesky_print_posts <number>");
                } else {
                    String blueskyToken = BlueskyGetToken.getToken(token);
                    BlueskyPrint.printPosts(blueskyToken, Integer.parseInt(parts[1]));
                }
            }

            case "bluesky_save_posts" -> {
                if (token.isBlank()) {
                    System.out.println("Bluesky token not found.");
                } else {
                    String blueskyToken = BlueskyGetToken.getToken(token);
                    DataRepository.saveBlueskyPosts(blueskyToken);
                }
            }

            case "bluesky_show_db" -> DataViewer.showBlueskyPosts();

            case "help" -> help();
            default -> System.out.println("Command not found");
        }
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
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            PUBCHEM
            pubchem_set_medicine: Set a medicine | medicine
            pubchem_print_all: Show all the information about a medicine
            pubchem_print_reactions: Show all the reactions about a medicine
            pubchem_print_mechanisms: Show all the mechanisms of action of a medicine

            pubchem_save_reactions: Save the current medicine and its reactions into the database
            pubchem_save_initial: Save the initial list of medicines from the API | token
            pubchem_add_medicines: Add multiple medicines to the database | medicine1 medicine2 ...
            pubchem_load_file: Load medicines from a file and save them into the database
            pubchem_show_medicines: Show all stored medicines
            pubchem_show_db: Show all stored PubChem reactions
            pubchem_show_summary: Show a summary of all stored medicines and reactions
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            BLUESKY
            bluesky_set_query: Set a search message | search
            bluesky_set_limit: Set a limit of results | number
            bluesky_set_start: Set a start date to search | date("YYYY-MM-DD")
            bluesky_set_end: Set an end date to search | date("YYYY-MM-DD")

            bluesky_print_posts: Show the number of past posts | number
            bluesky_save_posts: Save posts from Bluesky into the database
            bluesky_show_db: Show all stored Bluesky posts
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            """);
    }

}


