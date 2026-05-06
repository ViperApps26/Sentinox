package viper.sentinox.control;

import viper.sentinox.model.DataMart;
import viper.sentinox.control.subscriber.EventSubscriber;

public class BusinessUnitController {

    private final EventSubscriber subscriber;
    private final DataMart dataMart;

    public BusinessUnitController(EventSubscriber subscriber,
                                  DataMart dataMart) {
        this.subscriber = subscriber;
        this.dataMart = dataMart;
    }

    public void start() {
        System.out.println("Starting Business Unit...");
        subscriber.subscribe();
        System.out.println("Business Unit is running.");
        System.out.println(dataMart.getSummary());
    }
}