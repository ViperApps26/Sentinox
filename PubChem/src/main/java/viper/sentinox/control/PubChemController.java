package viper.sentinox.control;

import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.control.store.ActiveMQPubChemStore;
import viper.sentinox.model.PubChemEvent;

import java.io.IOException;
import java.util.List;

public class PubChemController {

    private final PubChemFeeder feeder;
    private final ActiveMQPubChemStore store;

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

    public PubChemController(PubChemFeeder feeder, ActiveMQPubChemStore store) {
        this.feeder = feeder;
        this.store = store;
    }

    public void execute() throws IOException {
        for (String medicine : medicines) {
            List<PubChemEvent> pubChemEvents = feeder.get(medicine);
            for (PubChemEvent pubChemEvent : pubChemEvents) {
                store.save(pubChemEvent);
            }
        }
    }
}