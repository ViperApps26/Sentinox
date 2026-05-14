package viper.sentinox.control;

import viper.sentinox.control.datamart.EventStoreReader;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;
import viper.sentinox.control.datamart.BusinessUnitEventHandler;

public class BusinessUnitEnvironment {

    public BusinessUnitController prepare(String brokerUrl, String clientID, MedicineDataMart dataMart) {
        BusinessUnitEventHandler handler = new BusinessUnitEventHandler(dataMart);

        BusinessUnitSubscriber subscriber = new BusinessUnitSubscriber(
                brokerUrl,
                clientID
        );

        EventStoreReader reader = new EventStoreReader(handler);

        return new BusinessUnitController(subscriber, handler, reader);
    }
}
