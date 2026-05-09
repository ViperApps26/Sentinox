package viper.sentinox.control.DataMartFeader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import viper.sentinox.model.DataMart;

import javax.jms.Message;
import javax.jms.TextMessage;

public class BusinessUnitEventHandler implements EventHandler {

    private final Gson gson = new Gson();
    private final DataMart dataMart;

    public BusinessUnitEventHandler(DataMart dataMart) {
        this.dataMart = dataMart;
    }

    public void handleMessage(Message message, String topicName) {
        try {
            if (message instanceof TextMessage textMessage) {
                String json = textMessage.getText();
                handleEvent(json, topicName);
            }
        } catch (Exception e) {
            System.out.println("Error handling event for topic " + topicName);
        }
    }

    @Override
    public void handleEvent(String json, String topicName) {
        JsonObject event = gson.fromJson(json, JsonObject.class);

        switch (topicName) {
            case "BlueskyPosts" -> handleBlueskyEvent(event);
            case "PubChemReactions" -> handlePubChemEvent(event);
            default -> System.out.println("Unknown topic received: " + topicName);
        }
    }

    private void handleBlueskyEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String comment = event.get("text").getAsString();
        String sentiment = event.get("sentiment").getAsString();

        dataMart.registerBlueskyEvent(medicine, comment, sentiment);
        System.out.println("Bluesky event registered for " + medicine);
    }

    private void handlePubChemEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String reaction = event.get("reaction").getAsString();

        dataMart.registerPubChemEvent(medicine, reaction);
        System.out.println("PubChem event registered for " + medicine);
    }
}
