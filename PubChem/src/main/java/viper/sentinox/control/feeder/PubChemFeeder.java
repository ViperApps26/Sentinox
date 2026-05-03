package viper.sentinox.control.feeder;

import viper.sentinox.control.PubChemConnector;
import viper.sentinox.control.PubChemGet;
import viper.sentinox.model.PubChemEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PubChemFeeder {

    private final PubChemConnector connector;
    private final PubChemGet get;

    public PubChemFeeder() {
        this.connector = new PubChemConnector();
        this.get = new PubChemGet(connector);
    }

    public List<PubChemEvent> get(String medicine) throws IOException {
        connector.setMedicine(medicine);

        return getEvents();
    }

    public List<PubChemEvent> getEvents() throws IOException {
        ArrayList<String> reactions = get.getReactions();

        String medicine = connector.getMedicine();
        String cid = connector.getCID();

        List<PubChemEvent> events = new ArrayList<>();
        addEvents(events, reactions, medicine, cid);
        return events;
    }

    private void addEvents(List<PubChemEvent> events, ArrayList<String> reactions, String medicine, String cid) {
        for (String reaction : reactions) {
            events.add(new PubChemEvent(
                            System.currentTimeMillis(),
                            "PubChemFeeder",
                            medicine,
                            cid,
                            reaction
                    )
            );
        }
    }
}