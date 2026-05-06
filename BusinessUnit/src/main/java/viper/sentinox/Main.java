package viper.sentinox;

import viper.sentinox.control.app.BusinessUnitApplication;

public class Main {
    static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Use: java viper.sentinox.Main <ActiveMQUrl>");
            return;
        }
        String brokerUrl = args[0];
        BusinessUnitApplication application = new BusinessUnitApplication();
        application.run(brokerUrl);
    }
}