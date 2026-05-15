package viper.sentinox.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventStoreBuilder implements EventStore {

    private final String brokerUrl;
    private final String clientId;
    private final String baseDir;

    private final Gson gson = new Gson();
    private final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of("UTC"));

    private static final Logger log = LoggerFactory.getLogger(EventStoreBuilder.class);

    public EventStoreBuilder(String brokerUrl, String clientID){
        this.brokerUrl = brokerUrl;
        clientId = clientID;
        baseDir = "eventstore";
    }

    public void store() {
        EventStoreSubscriber subscriber = new EventStoreSubscriber(brokerUrl, clientId);
        try {
            sendMessages(subscriber);
        } catch (Exception e) {
            log.error("Error in Event Store Builder");
        } finally {
            subscriber.close();
        }
    }

    private void sendMessages(EventStoreSubscriber subscriber) throws JMSException, InterruptedException {
        subscriber.connect();

        subscriber.subscribe("PubChemReactions", "EventStore_PubChem", message ->
                handleMessage(message, "PubChemReactions"));

        subscriber.subscribe("BlueskyPosts", "EventStore_Bluesky", message ->
                handleMessage(message, "BlueskyPosts"));

        subscriber.waitForever();
    }

    private void handleMessage(Message message, String topicName) {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                handleEvent(json, topicName);
            }
        } catch (Exception e) {
            log.error("Error handling event for topic {}", topicName);
        }
    }

    private void handleEvent(String json, String topicName) {
        try {
            JsonObject event = gson.fromJson(json, JsonObject.class);

            String ss = event.get("ss").getAsString();
            String date = dateFormatter.format(Instant.ofEpochMilli(event.get("ts").getAsLong()));

            File file = resolveEventFile(topicName, ss, date);
            writeEventToFile(file, json);

        } catch (Exception e) {
            log.error("Error handling event: {}", e.getMessage());
        }
    }

    private File resolveEventFile(String topicName, String ss, String date) {
        File path = new File(baseDir + File.separator + topicName + File.separator + ss);
        if (!path.exists() && !path.mkdirs()) {
            log.warn("Warning: could not create directory {}", path.getAbsolutePath());
        }
        return new File(path, date + ".events");
    }

    private void writeEventToFile(File file, String json) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(json);
            fw.write("\n");
            log.trace("Event stored in: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing event to file: {}", e.getMessage());
        }
    }
}

