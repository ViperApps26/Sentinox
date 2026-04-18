package viper.sentinox;

import java.io.IOException;

public class PubChemControl {

    private final PubChemFeeder pubChemFeeder;

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

    public PubChemControl(PubChemFeeder pubChemFeeder) {
        this.pubChemFeeder = pubChemFeeder;
    }

    public void execute() throws IOException {
        pubChemFeeder.feedReactionsFromList(medicines);
    }
}