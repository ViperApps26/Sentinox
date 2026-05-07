package viper.sentinox;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.model.MedicineDataMart;
import viper.sentinox.subscriber.ActiveMQBusinessSubscriber;
import viper.sentinox.view.ConsoleView;

public class Main {

    public static void main(String[] args) {

        String brokerUrl = args[0];

        ConsoleView view = new ConsoleView();
        MedicineDataMart dataMart = new MedicineDataMart();

        ActiveMQBusinessSubscriber subscriber = new ActiveMQBusinessSubscriber(
                brokerUrl,
                dataMart,
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