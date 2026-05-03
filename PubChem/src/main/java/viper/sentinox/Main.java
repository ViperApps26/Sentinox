package viper.sentinox;

import viper.sentinox.control.PubChemController;
import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.control.store.ActiveMQPubChemStore;
import viper.sentinox.control.PubChemConnect;
import viper.sentinox.control.PubChemGet;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        PubChemController pubChemControl = createPubChemEnvironment();

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, pubChemControl);
    }

    private static void autoExecute(ScheduledExecutorService scheduler, PubChemController control) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static PubChemController createPubChemEnvironment() {
        PubChemConnect connect = new PubChemConnect();
        PubChemGet get = new PubChemGet(connect);
        ActiveMQPubChemStore publisher = new ActiveMQPubChemStore(connect, get);
        PubChemFeeder feeder = new PubChemFeeder(publisher, connect);

        return new PubChemController(feeder);
    }
}