import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import viper.sentinox.control.PubChemController;
import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.control.store.ActiveMQPubChemStore;
import viper.sentinox.model.PubChemEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PubChemControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void loadMedicines_returnsMedicinesCorrectly() throws IOException {
        Path file = tempDir.resolve("medicines.txt");

        Files.writeString(file, """
                ibuprofen
                
                aspirin
                   naproxen
                """);

        FakeFeeder feeder = new FakeFeeder();
        FakeStore store = new FakeStore();

        PubChemController controller = new PubChemController(feeder, store);

        List<String> medicines = controller.loadMedicines(file);

        assertEquals(3, medicines.size());
        assertEquals("ibuprofen", medicines.get(0));
        assertEquals("aspirin", medicines.get(1));
        assertEquals("naproxen", medicines.get(2));
    }

    @Test
    void execute_getsEventsFromFeederAndSavesThemInStore() throws IOException {
        Path file = tempDir.resolve("medicines.txt");

        Files.writeString(file, """
                ibuprofen
                aspirin
                """);

        FakeFeeder feeder = new FakeFeeder();
        FakeStore store = new FakeStore();

        PubChemController controller = new PubChemController(feeder, store);

        controller.execute(file.toString());

        assertEquals(List.of("ibuprofen", "aspirin"), feeder.requestedMedicines);
        assertEquals(2, store.savedEvents.size());
        assertEquals("ibuprofen", store.savedEvents.get(0).medicine());
        assertEquals("aspirin", store.savedEvents.get(1).medicine());
    }

    private static class FakeFeeder extends PubChemFeeder {

        private final List<String> requestedMedicines = new ArrayList<>();

        @Override
        public List<PubChemEvent> get(String medicine) {
            requestedMedicines.add(medicine);

            return List.of(new PubChemEvent(
                    1L,
                    "PubChemFeeder",
                    medicine,
                    "fake-cid",
                    "fake-reaction"
            ));
        }
    }

    private static class FakeStore extends ActiveMQPubChemStore {

        private final List<PubChemEvent> savedEvents = new ArrayList<>();

        public FakeStore() {
            super("fake-url", "fake-topic");
        }

        @Override
        public void save(PubChemEvent event) {
            savedEvents.add(event);
        }
    }
}