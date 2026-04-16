package viper.sentinox;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String databaseURL = args[0];

        PubChemControl pubChemControl = createPubChemEnvironment();
        createDatabase(databaseURL);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        autoExecute(scheduler, pubChemControl, databaseURL);
    }

    private static void autoExecute(ScheduledExecutorService scheduler, PubChemControl pubChemControl, String databaseURL) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pubChemControl.execute(databaseURL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private static PubChemControl createPubChemEnvironment() {
        PubChemConnect connect = new PubChemConnect();
        PubChemGet get = new PubChemGet(connect);
        PubChemInsert insert = new PubChemInsert(connect, get);
        PubChemFeeder feeder = new PubChemFeeder(insert, connect);

        return new PubChemControl(feeder);
    }

    private static void createDatabase(String databaseURL) {
        PubChemDatabaseCreator pubChemDatabaseCreator = new PubChemDatabaseCreator();
        pubChemDatabaseCreator.createDatabase(databaseURL);
    }
}