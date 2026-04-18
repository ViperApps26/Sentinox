package viper.sentinox.control;

import java.io.IOException;

public class PubChemControl implements PubChemControlInterface{

    private final PubChemFeeder feeder;

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

    public PubChemControl(PubChemFeeder feeder) {
        this.feeder = feeder;
    }

    public void execute() throws IOException {
        feeder.feedReactionsFromList(medicines);
    }
}