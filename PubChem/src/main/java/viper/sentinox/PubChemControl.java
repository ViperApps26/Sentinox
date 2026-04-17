package viper.sentinox;

import java.io.IOException;

public class PubChemControl {

    private final PubChemFeeder pubChemFeeder;

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

    public PubChemControl(PubChemFeeder pubChemFeeder) {
        this.pubChemFeeder = pubChemFeeder;
    }

    public void execute() throws IOException {
        pubChemFeeder.feedReactionsFromList(medicines);
    }
}