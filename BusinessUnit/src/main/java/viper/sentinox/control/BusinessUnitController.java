package viper.sentinox.control;

import viper.sentinox.model.DataMart;
import viper.sentinox.subscriber.EventSubscriber;
import viper.sentinox.view.BusinessUnitView;

public class BusinessUnitController {

    private final EventSubscriber subscriber;
    private final DataMart dataMart;
    private final BusinessUnitView view;

    public BusinessUnitController(EventSubscriber subscriber,
                                  DataMart dataMart,
                                  BusinessUnitView view) {
        this.subscriber = subscriber;
        this.dataMart = dataMart;
        this.view = view;
    }

    public void start() {
        view.showMessage("Starting Business Unit...");
        subscriber.subscribe();
        view.showMessage("Business Unit is running.");
        view.showMessage(dataMart.getSummary());
    }
}