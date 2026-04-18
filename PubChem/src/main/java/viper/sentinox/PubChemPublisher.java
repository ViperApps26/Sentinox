package viper.sentinox;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.ArrayList;

public class PubChemPublisher {

    private final PubChemConnect pubChemConnect;
    private final PubChemGet pubChemGet;
    private final Gson gson;
    private final String brokerUrl;
    private final String topicName;

    public PubChemPublisher(PubChemConnect pubChemConnect, PubChemGet pubChemGet) {
        this.pubChemConnect = pubChemConnect;
        this.pubChemGet = pubChemGet;
        this.gson = new Gson();
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = "PubChemReactions";
    }

    public void publishReactions() throws IOException {
        ArrayList<String> reactions = pubChemGet.getReactions();

        String medicine = pubChemConnect.getMedicine();
        String cid = pubChemConnect.getCID();

        if (reactions.isEmpty()) {
            System.out.println(medicine + " has no reactions to publish");
            return;
        }

        for (String reaction : reactions) {
            PubChemEvent event = new PubChemEvent(
                    System.currentTimeMillis(),
                    "PubChemFeeder",
                    medicine,
                    cid,
                    reaction
            );

            String json = gson.toJson(event);
            publish(json);
        }

        System.out.println(medicine + " reactions published correctly");
    }

    public void publish(String jsonMessage) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);

            producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage(jsonMessage);

            producer.send(message);

            System.out.println("Message sent to topic " + topicName);

        } catch (Exception e) {
            System.out.println("Error publishing message to ActiveMQ");
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing ActiveMQ resources");
            }
        }
    }
}