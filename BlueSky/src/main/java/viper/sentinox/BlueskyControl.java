package viper.sentinox;

import java.io.IOException;

public class BlueskyControl {

    private final BlueskyFeeder blueskyFeeder;

    private final String[] medicines = {
            "ibuprofen",
            "aspirin",
            "naproxen",
            "paracetamol",
            "acetaminophen",
            "amoxicillin",
            "omeprazole",
            "metformin",
            "atorvastatin",
            "lisinopril",
            "sertraline",
            "fluoxetine",
            "diazepam",
            "morphine",
            "codeine"
    };

    public BlueskyControl(BlueskyFeeder blueskyFeeder) {
        this.blueskyFeeder = blueskyFeeder;
    }

    public void execute(String token, String password, String databaseURL)
            throws IOException, InterruptedException {
        blueskyFeeder.feedMedicinesFromList(medicines, token, password, databaseURL);
    }
}