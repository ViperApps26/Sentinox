package viper.sentinox;

import java.io.IOException;
import java.util.Scanner;

public class PubChemControl {

    private final PubChemConnect pubChemConnect;
    private final PubChemPrint pubChemPrint;
    private final PubChemFeeder pubChemFeeder;
    private final PubChemDataViewer pubChemDataViewer;

    public PubChemControl(PubChemConnect pubChemConnect,
                          PubChemPrint pubChemPrint,
                          PubChemFeeder pubChemFeeder,
                          PubChemDataViewer pubChemDataViewer) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemPrint = pubChemPrint;
        this.pubChemFeeder = pubChemFeeder;
        this.pubChemDataViewer = pubChemDataViewer;
    }

    public void processCommand(String command, String databaseURL) throws IOException {
        String[] parts = command.split(" ");

        switch (parts[0]) {
            case "pubchem_set_medicine" -> {
                if (validate(parts, 2, 2, -1)) {
                    pubChemConnect.setMedicine(parts[1]);
                }
            }

            case "pubchem_print_all" -> pubChemPrint.printAllInfo();
            case "pubchem_print_reactions" -> pubChemPrint.printReactions();
            case "pubchem_print_mechanisms" -> pubChemPrint.printMechanism();

            case "pubchem_save_reactions" -> pubChemFeeder.feedCurrentMedicine(databaseURL);

            case "pubchem_show_medicines" -> pubChemDataViewer.showMedicines(databaseURL);
            case "pubchem_show_reactions" -> pubChemDataViewer.showReactions(databaseURL);
            case "pubchem_show_summary" -> pubChemDataViewer.showSummary(databaseURL);

            case "help" -> help();

            default -> System.out.println("Command not found");
        }
    }

    public boolean validate(String[] parts, int minLength, int maxLength, int positionToCheck) {
        if (parts.length < minLength || parts.length > maxLength) {
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
            GENERAL
            exit: Exit the program
            help: Show this help menu

            PUBCHEM
            pubchem_set_medicine: Set a medicine | medicine
            pubchem_print_all: Show all the information about a medicine
            pubchem_print_reactions: Show all the reactions about a medicine
            pubchem_print_mechanisms: Show all the mechanisms of action of a medicine
            pubchem_save_reactions: Save the current medicine and its reactions into the database
            pubchem_show_medicines: Show all stored medicines
            pubchem_show_reactions: Show all stored PubChem reactions
            pubchem_show_summary: Show a summary of all stored medicines and reactions
            - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            """);
    }
}