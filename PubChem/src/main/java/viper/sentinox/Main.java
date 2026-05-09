package viper.sentinox;

import viper.sentinox.control.PubChemController;
import viper.sentinox.control.feeder.PubChemFeeder;
import viper.sentinox.control.store.ActiveMQPubChemStore;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Use: java viper.sentinox.Main <ActiveMQUrl> <topicName> <medicinesListPath>");
            return;
        }
        String url = args[0];
        String topic = args[1];
        String medicinesListPath = args[2];


        PubChemController pubChemControl = createPubChemEnvironment(url, topic);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, pubChemControl, medicinesListPath);
    }

    private static void autoExecute(ScheduledExecutorService scheduler, PubChemController control, String medicinesListPath) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                control.execute(medicinesListPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static PubChemController createPubChemEnvironment(String url, String topic) {
        ActiveMQPubChemStore store = new ActiveMQPubChemStore(url, topic);
        PubChemFeeder feeder = new PubChemFeeder();

        return new PubChemController(feeder, store);
    }
}