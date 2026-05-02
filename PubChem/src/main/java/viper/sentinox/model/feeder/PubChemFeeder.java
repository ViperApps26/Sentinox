package viper.sentinox.model.feeder;

import viper.sentinox.model.PubChemConnect;
import viper.sentinox.model.store.ActiveMQPubChemStore;

import java.io.IOException;

public class PubChemFeeder implements PubChemFeederInterface {

    private final ActiveMQPubChemStore publisher;
    private final PubChemConnect connect;

    public PubChemFeeder(ActiveMQPubChemStore publisher, PubChemConnect connect) {
        this.publisher = publisher;
        this.connect = connect;
    }

    public void feedReactionsFromList(String[] medicines) throws IOException {
        for (String medicine : medicines) {
            connect.setMedicine(medicine);
            publisher.publishReactions();
        }
    }
}