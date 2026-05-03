package viper.sentinox.subscriber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import viper.sentinox.model.DataMart;
import viper.sentinox.view.BusinessUnitView;

public class BusinessEventHandler implements EventHandler {

    private final Gson gson;
    private final DataMart dataMart;
    private final BusinessUnitView view;

    public BusinessEventHandler(DataMart dataMart, BusinessUnitView view) {
        this.gson = new Gson();
        this.dataMart = dataMart;
        this.view = view;
    }

    @Override
    public void handle(String topicName, String json) {
        JsonObject event = gson.fromJson(json, JsonObject.class);

        if ("BlueskyPosts".equals(topicName)) {
            handleBlueskyEvent(event);
        } else if ("PubChemReactions".equals(topicName)) {
            handlePubChemEvent(event);
        } else {
            view.showMessage("Unknown topic received: " + topicName);
        }
    }

    private void handleBlueskyEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String sentiment = event.get("sentiment").getAsString();

        dataMart.registerSentiment(medicine, sentiment);
        view.showMessage("Bluesky event registered for " + medicine);
    }

    private void handlePubChemEvent(JsonObject event) {
        String medicine = event.get("medicine").getAsString();
        String reaction = event.get("reaction").getAsString();

        dataMart.registerReaction(medicine, reaction);
        view.showMessage("PubChem event registered for " + medicine);
    }
}