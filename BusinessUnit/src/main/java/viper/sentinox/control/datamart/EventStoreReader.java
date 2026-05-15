package viper.sentinox.control.datamart;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStoreReader implements LoadHistoricalEvents {

    private final BusinessUnitEventHandler handler;
    private final File eventStoreRoot;

    private static final Logger log = LoggerFactory.getLogger(EventStoreReader.class);

    public EventStoreReader(BusinessUnitEventHandler handler) {
        this.handler = handler;
        this.eventStoreRoot = new File("eventstore");
    }

    public void loadHistoricalEvents() {
        if (!eventStoreRoot.exists()) {
            log.warn("Event store directory not found: {}", eventStoreRoot.getAbsolutePath());
            return;
        }
        log.debug("Loading historical events from: {}", eventStoreRoot.getAbsolutePath());

        loadTopic("BlueskyPosts");
        loadTopic("PubChemReactions");

        log.info("Historical events loaded successfully.");
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
        log.debug("Reading file: {}, from: {}", file.getName(), topicName);

        try (Stream<String> lines = Files.lines(file.toPath())) {
            lines.forEach(line -> handler.handleEvent(line, topicName));
        } catch (Exception e) {
            log.error("Error reading event file: {}", file.getName());
        }
    }
}

