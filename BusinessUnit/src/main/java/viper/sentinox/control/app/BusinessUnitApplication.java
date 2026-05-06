package viper.sentinox.control.app;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.model.DataMart;
import viper.sentinox.model.InMemoryMedicineDataMart;
import viper.sentinox.control.subscriber.ActiveMQEventSubscriber;
import viper.sentinox.control.subscriber.BusinessEventHandler;
import viper.sentinox.control.subscriber.EventSubscriber;

public class BusinessUnitApplication {

    public void run(String brokerUrl) {
        DataMart dataMart = new InMemoryMedicineDataMart();

        EventSubscriber subscriber = new ActiveMQEventSubscriber(
                brokerUrl,
                "BusinessUnit",
                "BusinessUnitSubscription",
                new BusinessEventHandler(dataMart)
        );

        BusinessUnitController controller = new BusinessUnitController(
                subscriber,
                dataMart
        );

        controller.start();
    }
}