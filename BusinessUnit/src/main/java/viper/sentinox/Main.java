package viper.sentinox;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.control.BusinessUnitEnvironment;
import viper.sentinox.control.datamart.MedicineDataMart;
import viper.sentinox.view.ViperApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            log.error("Use: java viper.sentinox.Main <ActiveMQUrl> <clientID>");
            return;
        }
        String brokerUrl = args[0];
        String clientID = args[1];

        start(brokerUrl, clientID, args);
    }

    private static void start(String brokerUrl, String clientID, String[] args) throws IOException {
        MedicineDataMart dataMart = new MedicineDataMart();
        BusinessUnitEnvironment environment = new BusinessUnitEnvironment();
        BusinessUnitController controller = environment.prepare(brokerUrl, clientID, dataMart);
        updateDatamart(controller);

        ViperApp.setDataMart(dataMart);
        javafx.application.Application.launch(ViperApp.class, args);
    }

    private static void updateDatamart(BusinessUnitController controller) {
        Thread backendThread = new Thread(controller::start);
        backendThread.setDaemon(true);
        backendThread.start();
    }
}