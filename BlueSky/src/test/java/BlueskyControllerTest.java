import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import viper.sentinox.control.BlueskyController;
import viper.sentinox.control.feeder.BlueskyFeeder;
import viper.sentinox.control.store.ActiveMQBlueskyStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlueskyControllerTest {

    private BlueskyController controller;

    private BlueskyFeeder feeder;
    private ActiveMQBlueskyStore store;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        feeder = mock(BlueskyFeeder.class);
        store = mock(ActiveMQBlueskyStore.class);

        controller = new BlueskyController(feeder, store);
    }

    @Test
    void loadMedicines_returnsMedicinesCorrectly() throws IOException {

        Path file = tempDir.resolve("medicines.txt");

        Files.writeString(file, """
                ibuprofen
                aspirin
                naproxen
                """);

        List<String> medicines = controller.loadMedicines(file);

        assertEquals(3, medicines.size());

        assertEquals("ibuprofen", medicines.get(0));
        assertEquals("aspirin", medicines.get(1));
        assertEquals("naproxen", medicines.get(2));
    }
}