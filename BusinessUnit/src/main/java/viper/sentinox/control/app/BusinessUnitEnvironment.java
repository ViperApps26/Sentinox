package viper.sentinox.control.app;

import viper.sentinox.control.BusinessUnitAPI;
import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.control.MedicineDataMart;
import viper.sentinox.control.subscriber.BusinessUnitSubscriber;
import viper.sentinox.control.subscriber.BusinessUnitEventHandler;

public class BusinessUnitEnvironment {

    public BusinessUnitController prepare(String brokerUrl, String clientID) {
        MedicineDataMart dataMart = new MedicineDataMart();

        BusinessUnitEventHandler handler = new BusinessUnitEventHandler(dataMart);

        BusinessUnitSubscriber subscriber = new BusinessUnitSubscriber(
                brokerUrl,
                clientID
        );

        BusinessUnitAPI api = new BusinessUnitAPI(dataMart);

        return new BusinessUnitController(subscriber, handler, api);
    }
}
