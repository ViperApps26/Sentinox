package viper.sentinox.control;

import viper.sentinox.control.DataMartFeader.EventStoreReader;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;
import viper.sentinox.control.DataMartFeader.BusinessUnitEventHandler;
import viper.sentinox.view.BusinessUnitAPI;

public class BusinessUnitEnvironment {

    public BusinessUnitController prepare(String brokerUrl, String clientID) {
        MedicineDataMart dataMart = new MedicineDataMart();

        BusinessUnitEventHandler handler = new BusinessUnitEventHandler(dataMart);

        BusinessUnitSubscriber subscriber = new BusinessUnitSubscriber(
                brokerUrl,
                clientID
        );

        BusinessUnitAPI api = new BusinessUnitAPI(dataMart);

        EventStoreReader reader = new EventStoreReader(handler);

        return new BusinessUnitController(subscriber, handler, api, reader);
    }
}
