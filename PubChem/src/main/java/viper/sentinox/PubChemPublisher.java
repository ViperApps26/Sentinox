package viper.sentinox;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class PubChemPublisher {

    private final String brokerUrl;
    private final String topicName;

    public PubChemPublisher() {
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = "PubChemReactions";
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }
}