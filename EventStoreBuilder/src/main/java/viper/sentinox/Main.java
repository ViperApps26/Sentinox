package viper.sentinox;

import viper.sentinox.control.EventStoreBuilder;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Use: java viper.sentinox.Main <brokerURL> <clientID>");
            return;
        }
        String brokerURL = args[0];
        String clientID = args[1];

        start(brokerURL, clientID);
    }

    private static void start(String brokerURL, String clientID) {
        System.out.println("Starting Event Store Builder...");
        EventStoreBuilder builder = new EventStoreBuilder(brokerURL, clientID);
        builder.store();
    }
}