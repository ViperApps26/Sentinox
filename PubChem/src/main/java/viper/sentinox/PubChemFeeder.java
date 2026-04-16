package viper.sentinox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PubChemFeeder {

    private final PubChemInsert pubChemInsert;
    private final PubChemConnect pubChemConnect;

    public PubChemFeeder(PubChemInsert pubChemInsert, PubChemConnect pubChemConnect) {
        this.pubChemInsert = pubChemInsert;
        this.pubChemConnect = pubChemConnect;
    }


    public void feedMedicinesFromList(String[] medicines, String databaseURL) throws IOException {
        for (String medicine : medicines) {
            pubChemConnect.setMedicine(medicine);
            pubChemInsert.saveMedicine(databaseURL);
        }
    }

    public void feedReactions(String databaseURL) throws IOException {
        List<String> medicines = new ArrayList<>();
        pubChemInsert.getMedicinesList(medicines, databaseURL);

        for (String medicine : medicines) {
            pubChemConnect.setMedicine(medicine);
            pubChemInsert.saveReactions(databaseURL);
        }
    }
}