package viper.sentinox;

import java.io.IOException;
import java.util.Arrays;

public class PubChemCommand {

    private final PubChemConnect pubChemConnect;
    private final PubChemPrint pubChemPrint;

    public PubChemCommand(PubChemConnect pubChemConnect,
                          PubChemPrint pubChemPrint) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemPrint = pubChemPrint;
    }

    public void execute(String[] parts, String databaseURL) throws IOException {

        switch (parts[0]) {

            case "pubchem_set_medicine" -> {
                if (validate(parts, 2)) {
                    pubChemConnect.setMedicine(parts[1]);
                }
            }

            case "pubchem_print_all" -> pubChemPrint.printAllInfo();

            case "pubchem_print_reactions" -> pubChemPrint.printReactions();

            case "pubchem_print_mechanisms" -> pubChemPrint.printMechanism();

            case "pubchem_save_reactions" ->
                    DatabaseInsert.saveReactions(databaseURL);

            case "pubchem_save_medicines" -> {
                if (parts.length >= 2) {
                    String[] medicines = Arrays.copyOfRange(parts, 1, parts.length);
                    DatabaseInsert.saveMedicinesFromList(medicines, databaseURL);
                }
            }

            case "pubchem_show_medicines" ->
                    DataViewer.showMedicines(databaseURL);

            case "pubchem_show_reactions" ->
                    DataViewer.showPubChemReactions(databaseURL);

            case "pubchem_show_summary" ->
                    DataViewer.showMedicineSummary(databaseURL);

            case "pubchem_help" -> help();
        }
    }

    public void help() {
        System.out.println("""
            ------------------------- PUBCHEM ----------------------

            pubchem_set_medicine <medicine>: Set the current medicine
            pubchem_print_all: Print all information about the medicine
            pubchem_print_reactions: Print adverse effects
            pubchem_print_mechanisms: Print mechanisms of action
            pubchem_save_reactions: Save reactions into the database
            pubchem_save_medicines <med1> <med2> ... : Save multiple medicines
            pubchem_show_medicines: Show stored medicines
            pubchem_show_reactions: Show stored reactions
            pubchem_show_summary: Show summary of data

            -------------------------------------------------------
            """);
    }

    private boolean validate(String[] parts, int expectedLength) {
        if (parts.length != expectedLength) {
            System.out.println("Incorrect command length");
            return false;
        }
        return true;
    }
}
