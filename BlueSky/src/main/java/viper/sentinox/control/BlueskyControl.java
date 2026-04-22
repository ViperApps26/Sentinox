package viper.sentinox.control;

import viper.sentinox.control.feeder.BlueskyFeeder;
import viper.sentinox.control.store.BlueskyStore;
import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.util.List;

public class BlueskyControl {

    private final BlueskyFeeder feeder;
    private final BlueskyStore store;

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

    public BlueskyControl(BlueskyFeeder blueskyFeeder, BlueskyStore store) {
        this.feeder = blueskyFeeder;
        this.store = store;
    }

    public void execute()
            throws IOException, InterruptedException {
        for (String medicine : medicines) {
            List<BlueskyEvent> blueskyEvents = feeder.get(medicine);
            for (BlueskyEvent blueskyEvent : blueskyEvents) {
                store.save(blueskyEvent);
            }
        }
    }
}