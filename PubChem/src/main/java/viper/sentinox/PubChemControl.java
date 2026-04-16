package viper.sentinox;

import java.io.IOException;

public class PubChemControl {

    private final PubChemFeeder pubChemFeeder;
    private final String[] medicines = {"ibuprofen", "aspirin", "naproxen", "codeine", "morphine"};

    public PubChemControl(PubChemFeeder pubChemFeeder) {
        this.pubChemFeeder = pubChemFeeder;
    }


    public void execute(String databaseURL) throws IOException {
        pubChemFeeder.feedMedicinesFromList(medicines, databaseURL);
        pubChemFeeder.feedReactions(databaseURL);
    }
}