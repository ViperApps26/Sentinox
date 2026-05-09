package viper.sentinox.control;

import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.control.store.ActiveMQPubChemStore;
import viper.sentinox.model.PubChemEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PubChemController {

    private final PubChemFeeder feeder;
    private final ActiveMQPubChemStore store;

    public PubChemController(PubChemFeeder feeder, ActiveMQPubChemStore store) {
        this.feeder = feeder;
        this.store = store;
    }


    public List<String> loadMedicines(Path path) throws IOException {
        return Files.readAllLines(path)
                .stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }

    public void execute(String medicinesListPath) throws IOException {
        List<String> medicines = loadMedicines(Paths.get(medicinesListPath));

        for (String medicine : medicines) {
            List<PubChemEvent> pubChemEvents = feeder.get(medicine);
            for (PubChemEvent pubChemEvent : pubChemEvents) {
                store.save(pubChemEvent);
            }
        }
    }
}