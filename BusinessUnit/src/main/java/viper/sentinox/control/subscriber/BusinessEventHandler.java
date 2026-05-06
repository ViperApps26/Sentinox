package viper.sentinox.control.subscriber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import viper.sentinox.model.DataMart;

public class BusinessEventHandler implements EventHandler {

    private final Gson gson;
    private final DataMart dataMart;

    public BusinessEventHandler(DataMart dataMart) {
        this.gson = new Gson();
        this.dataMart = dataMart;
    }

    @Override
    public void handle(String topicName, String json) {
        JsonObject event = gson.fromJson(json, JsonObject.class);

        if ("BlueskyPosts".equals(topicName)) {
            handleBlueskyEvent(event);
        } else if ("PubChemReactions".equals(topicName)) {
            handlePubChemEvent(event);
        } else {
            System.out.println("Unknown topic received: " + topicName);
        }
    }

    private void handleBlueskyEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String sentiment = event.get("sentiment").getAsString();

        dataMart.registerSentiment(medicine, sentiment);
        System.out.println("Bluesky event registered for " + medicine);
    }

    private void handlePubChemEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String reaction = event.get("reaction").getAsString();

        dataMart.registerReaction(medicine, reaction);
        System.out.println("PubChem event registered for " + medicine);
    }
}