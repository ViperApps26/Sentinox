package viper.sentinox.control;

import viper.sentinox.control.subscriber.BusinessUnitEventHandler;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;

import javax.jms.JMSException;

public class BusinessUnitController {

    private final BusinessUnitSubscriber subscriber;
    private final BusinessUnitEventHandler handler;

    public BusinessUnitController(BusinessUnitSubscriber subscriber, BusinessUnitEventHandler handler) {
        this.subscriber = subscriber;
        this.handler = handler;
    }

    public void start() {
        try {
            storeMessages();
        } catch (Exception e) {
            System.out.println("Error in Business Unit: " + e.getMessage());
        } finally {
            subscriber.close();
        }
    }

    public void storeMessages() throws JMSException, InterruptedException {
        subscriber.connect();

        subscriber.subscribe("BlueskyPosts", "BusinessUnit_Bluesky", message ->
                handler.handleMessage(message, "BlueskyPosts"));

        subscriber.subscribe("PubChemReactions", "BusinessUnit_PubChem", message ->
                handler.handleMessage(message, "PubChemReactions"));

        subscriber.waitForever();
    }
}
