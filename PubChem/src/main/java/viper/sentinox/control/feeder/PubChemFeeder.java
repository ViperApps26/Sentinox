package viper.sentinox.control.feeder;

import viper.sentinox.control.PubChemConnect;
import viper.sentinox.control.store.ActiveMQPubChemStore;

import java.io.IOException;

public class PubChemFeeder {

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