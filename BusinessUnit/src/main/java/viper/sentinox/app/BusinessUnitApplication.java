package viper.sentinox.app;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.model.DataMart;
import viper.sentinox.model.InMemoryMedicineDataMart;
import viper.sentinox.subscriber.ActiveMQEventSubscriber;
import viper.sentinox.subscriber.BusinessEventHandler;
import viper.sentinox.subscriber.EventHandler;
import viper.sentinox.subscriber.EventSubscriber;
import viper.sentinox.view.BusinessUnitView;
import viper.sentinox.view.ConsoleBusinessUnitView;

public class BusinessUnitApplication {

    public void run(String[] args) {
        String brokerUrl = args.length > 0 ? args[0] : "tcp://localhost:61616";

        BusinessUnitView view = new ConsoleBusinessUnitView();
        DataMart dataMart = new InMemoryMedicineDataMart();
        EventHandler eventHandler = new BusinessEventHandler(dataMart, view);

        EventSubscriber subscriber = new ActiveMQEventSubscriber(
                brokerUrl,
                "BusinessUnit",
                "BusinessUnitSubscription",
                eventHandler,
                view
        );

        BusinessUnitController controller = new BusinessUnitController(
                subscriber,
                dataMart,
                view
        );

        controller.start();
    }
}