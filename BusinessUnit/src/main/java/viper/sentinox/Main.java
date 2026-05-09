package viper.sentinox;

import viper.sentinox.control.BusinessUnitController;
import viper.sentinox.control.BusinessUnitEnvironment;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Use: java viper.sentinox.Main <ActiveMQUrl> <clientID>");
            return;
        }
        String brokerUrl = args[0];
        String clientID = args[1];

        start(brokerUrl, clientID);
    }

    private static void start(String brokerUrl, String clientID) {
        BusinessUnitEnvironment environment = new BusinessUnitEnvironment();
        BusinessUnitController controller = environment.prepare(brokerUrl, clientID);
        controller.start();
    }
}