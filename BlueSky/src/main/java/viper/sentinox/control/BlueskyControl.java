package viper.sentinox.control;

import java.io.IOException;

public class BlueskyControl {

    private final BlueskyFeeder feeder;

    private final String[] medicines = {
            "ibuprofen",
            "paracetamol",
            "aspirin",
            "naproxen",
            "amoxicillin",
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
        this.feeder = blueskyFeeder;
    }

    public void execute(String token, String password)
            throws IOException, InterruptedException {
        feeder.feedMedicinesFromList(medicines, token, password);
    }
}