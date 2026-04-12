package viper.sentinox;

import java.io.IOException;

public class PubChemFeeder {

    private final PubChemInsert pubChemInsert;

    public PubChemFeeder(PubChemInsert pubChemInsert) {
        this.pubChemInsert = pubChemInsert;
    }

    public void feedCurrentMedicine(String databaseURL) throws IOException {
        pubChemInsert.saveReactions(databaseURL);
        System.out.println("PubChem data stored correctly");
    }

    public void feedMedicinesFromList(String[] medicines, String databaseURL) throws IOException {
        pubChemInsert.saveMedicinesFromList(medicines, databaseURL);
        System.out.println("Medicines stored correctly");
    }
}