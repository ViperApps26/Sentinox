package viper.sentinox;

import viper.sentinox.control.EventStoreBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("Use: java viper.sentinox.Main <brokerURL> <clientID>");
            return;
        }
        String brokerURL = args[0];
        String clientID = args[1];

        start(brokerURL, clientID);
    }

    private static void start(String brokerURL, String clientID) {
        log.info("Starting Event Store Builder...");
        EventStoreBuilder builder = new EventStoreBuilder(brokerURL, clientID);
        builder.store();
    }
}