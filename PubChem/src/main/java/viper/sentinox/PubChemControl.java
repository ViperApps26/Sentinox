package viper.sentinox;

import java.io.IOException;
import java.util.Scanner;

public class PubChemControl {

    private final PubChemConnect pubChemConnect;
    private final PubChemPrint pubChemPrint;

    public PubChemControl(PubChemConnect pubChemConnect, PubChemPrint pubChemPrint) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemPrint = pubChemPrint;
    }

    public void processCommand(String command, String databaseURL) throws IOException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "pubchem_set_medicine" -> {
                if (validate(parts, 2, 2,-1)) {
                    pubChemConnect.setMedicine(parts[1]);
                }
            }

            case "pubchem_print_all" -> pubChemPrint.printAllInfo();
            case "pubchem_print_reactions" -> pubChemPrint.printReactions();
            case "pubchem_print_mechanisms" -> pubChemPrint.printMechanism();

            /*
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
            }
            */
            case "help" -> help();
            default -> System.out.println("Command not found");
        }
    }

    public boolean validate(String[] parts, int minLength, int maxLength, int positionToCheck) {
        if (parts.length < minLength | parts.length > maxLength) {
            System.out.println("Incorrect command length");
            return false;
        } else if (positionToCheck >= 0 && !parts[positionToCheck].matches("\\d+")) {
            System.out.println("Incorrect attribute type");
            return false;
        }

        return true;
    }

    public String askCommand(Scanner scanner) {
        System.out.println("Write a command\n(Write \"help\" if you don't know any command)");
        return scanner.nextLine().trim();
    }

    public void help() {
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
            """);
    }
}
