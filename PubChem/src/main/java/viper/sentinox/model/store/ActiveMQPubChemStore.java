package viper.sentinox.model.store;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.model.PubChemConnect;
import viper.sentinox.model.PubChemEvent;
import viper.sentinox.model.PubChemGet;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;

public class ActiveMQPubChemStore implements PubChemStore {

    private final PubChemConnect connect;
    private final PubChemGet get;
    private final Gson gson;
    private final String brokerUrl;
    private final String topicName;

    public ActiveMQPubChemStore(PubChemConnect connect, PubChemGet get) {
        this.connect = connect;
        this.get = get;
        this.gson = new Gson();
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = "PubChemReactions";
    }

    public void publishReactions() throws IOException {
        ArrayList<String> reactions = get.getReactions();

        String medicine = connect.getMedicine();
        String cid = connect.getCID();

        if (reactions.isEmpty()) {
            System.out.println(medicine + " has no reactions to publish");
        } else {
            publishEachEvent(reactions, medicine, cid);
            System.out.println(medicine + " reactions published correctly");
        }
    }

    private void publishEachEvent(ArrayList<String> reactions, String medicine, String cid) {
        for (String reaction : reactions) {
            PubChemEvent event = new PubChemEvent(
                    System.currentTimeMillis(),
                    "PubChemFeeder",
                    medicine,
                    cid,
                    reaction
            );
            publish(gson.toJson(event));
        }
    }

    private void publish(String jsonMessage) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = createConnection();
            session = createSession(connection);
            producer = createProducer(session);

            sendMessage(session, producer, jsonMessage);
        } catch (Exception e) {
            System.out.println("Error publishing message to ActiveMQ");
            e.printStackTrace();
        } finally {
            closeResources(producer, session, connection);
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private MessageProducer createProducer(Session session) throws JMSException {
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        return producer;
    }

    private void sendMessage(Session session, MessageProducer producer, String jsonMessage) throws JMSException {
        TextMessage message = session.createTextMessage(jsonMessage);
        producer.send(message);
        System.out.println("Message sent to topic " + topicName);
    }

    private static void closeResources(MessageProducer producer, Session session, Connection connection) {
        try {
            if (producer != null) { producer.close(); }
            if (session != null) { session.close(); }
            if (connection != null) { connection.close(); }
        } catch (Exception e) {
            System.out.println("Error closing ActiveMQ resources");
        }
    }
}