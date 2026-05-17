package viper.sentinox.control;

import viper.sentinox.control.feeder.BlueskyFeeder;
import viper.sentinox.control.store.BlueskyEventStoreConsumer;
import viper.sentinox.model.BlueskyEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BlueskyController {

    private final BlueskyFeeder feeder;
    private final BlueskyEventStoreConsumer store;

    public BlueskyController(BlueskyFeeder blueskyFeeder, BlueskyEventStoreConsumer store) {
        this.feeder = blueskyFeeder;
        this.store = store;
    }


    public List<String> loadMedicines(Path path) throws IOException {
        return Files.readAllLines(path)
                .stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    public void execute(String medicinesListPath)
            throws IOException, InterruptedException {
        List<String> medicines = loadMedicines(Paths.get(medicinesListPath));

        for (String medicine : medicines) {
            List<BlueskyEvent> blueskyEvents = feeder.get(medicine);
            for (BlueskyEvent blueskyEvent : blueskyEvents) {
                store.save(blueskyEvent);
            }
        }
    }
}