package viper.sentinox.control.app;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.model.MedicineDataMart;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;
import viper.sentinox.control.subscriber.BusinessUnitEventHandler;

public class BusinessUnitEnvironment {

    public BusinessUnitController prepare(String brokerUrl, String clientID) {
        BusinessUnitEventHandler handler = new BusinessUnitEventHandler(
                new MedicineDataMart()
                );

        BusinessUnitSubscriber subscriber = new BusinessUnitSubscriber(
                brokerUrl,
                clientID
        );

        return new BusinessUnitController(subscriber, handler);
    }
}
