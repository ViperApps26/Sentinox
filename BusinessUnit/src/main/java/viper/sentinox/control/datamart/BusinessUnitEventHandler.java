package viper.sentinox.control.datamart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessUnitEventHandler implements EventHandler {

    private final Gson gson = new Gson();
    private final MedicineDataMart dataMart;

    private static final Logger log = LoggerFactory.getLogger(BusinessUnitEventHandler.class);

    public BusinessUnitEventHandler(MedicineDataMart dataMart) {
        this.dataMart = dataMart;
    }

    public void handleMessage(Message message, String topicName) {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                handleEvent(json, topicName);
            }
        } catch (Exception e) {
            log.error("Error handling event for topic {}", topicName);
        }
    }

    @Override
    public void handleEvent(String json, String topicName) {
        JsonObject event = gson.fromJson(json, JsonObject.class);

        switch (topicName) {
            case "BlueskyPosts" -> handleBlueskyEvent(event);
            case "PubChemReactions" -> handlePubChemEvent(event);
            default -> log.warn("Unknown topic received: {}", topicName);
        }
    }

    private void handleBlueskyEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String author = event.get("author").getAsString();
        String text = event.get("text").getAsString();
        String sentiment = event.get("sentiment").getAsString();
        Instant date = Instant.parse(event.get("createdAt").getAsString());

        dataMart.registerBlueskyEvent(medicine, author, text, sentiment, date);
        log.trace("Bluesky event registered for {}", medicine);
    }

    private void handlePubChemEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String reaction = event.get("reaction").getAsString();

        dataMart.registerPubChemEvent(medicine, reaction);
        log.trace("PubChem event registered for {}", medicine);
    }
}
