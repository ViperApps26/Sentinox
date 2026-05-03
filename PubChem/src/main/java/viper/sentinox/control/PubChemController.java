package viper.sentinox.control;

import viper.sentinox.control.feeder.PubChemFeeder;

import java.io.IOException;

public class PubChemController implements PubChemControlInterface {

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

    public PubChemController(PubChemFeeder feeder) {
        this.feeder = feeder;
    }

    public void execute() throws IOException {
        feeder.feedReactionsFromList(medicines);
    }
}