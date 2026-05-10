package viper.sentinox.control;

import viper.sentinox.control.DataMartFeader.BusinessUnitEventHandler;
import viper.sentinox.control.DataMartFeader.EventStoreReader;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;

import javax.jms.JMSException;

public class BusinessUnitController {

    private final BusinessUnitSubscriber subscriber;
    private final BusinessUnitEventHandler handler;
    private final EventStoreReader eventStoreReader;

    public BusinessUnitController(BusinessUnitSubscriber subscriber,
                                  BusinessUnitEventHandler handler,
                                  EventStoreReader eventStoreReader) {
        this.subscriber = subscriber;
        this.handler = handler;
        this.eventStoreReader = eventStoreReader;
    }

    public void start() {
        try {
            eventStoreReader.loadHistoricalEvents();
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
