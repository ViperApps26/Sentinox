package viper.sentinox.control.DataMartFeader;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Stream;

public class EventStoreReader implements LoadHistoricalEvents {

    private final BusinessUnitEventHandler handler;
    private final File eventStoreRoot;

    public EventStoreReader(BusinessUnitEventHandler handler) {
        this.handler = handler;
        this.eventStoreRoot = new File("eventstore");
    }

    public void loadHistoricalEvents() {
        if (!eventStoreRoot.exists()) {
            System.out.println("Event store directory not found: " + eventStoreRoot.getAbsolutePath());
            return;
        }
        System.out.println("Loading historical events from: " + eventStoreRoot.getAbsolutePath());

        loadTopic("BlueskyPosts");
        loadTopic("PubChemReactions");

        System.out.println("Historical events loaded successfully.");
    }

    private void loadTopic(String topicName) {
        File topicDir = new File(eventStoreRoot, topicName);
        if (!topicDir.exists()) return;

        for (File feederDir : Objects.requireNonNull(topicDir.listFiles())) {
            for (File eventFile : Objects.requireNonNull(feederDir.listFiles())) {
                loadEventFile(eventFile, topicName);
            }
        }
    }

    private void loadEventFile(File file, String topicName) {
        System.out.println("Reading file: " + file.getName() + ", from: " + topicName);

        try (Stream<String> lines = Files.lines(file.toPath())) {
            lines.forEach(line -> handler.handleEvent(line, topicName));
        } catch (Exception e) {
            System.out.println("Error reading event file: " + file.getName());
        }
    }
}

