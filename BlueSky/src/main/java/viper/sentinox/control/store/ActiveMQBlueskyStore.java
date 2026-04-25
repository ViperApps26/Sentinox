package viper.sentinox.control.store;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.control.BlueskyConnector;
import viper.sentinox.model.BlueskyEvent;

import javax.jms.*;

public class ActiveMQBlueskyStore implements BlueskyStore {

    private final Gson gson;
    private final String url;
    private final String topic;
    private final BlueskyConnector connector;

    public ActiveMQBlueskyStore(String url,
                                String topic) {
        this.url = url;
        this.topic = topic;
        this.gson = new Gson();
        this.connector = new BlueskyConnector();
    }


    public void save(BlueskyEvent event) {
        publish(gson.toJson(event));
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
        } finally {
            closeResources(producer, session, connection);
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private MessageProducer createProducer(Session session) throws JMSException {
        Destination destination = session.createTopic(topic);
        return session.createProducer(destination);
    }

    private void sendMessage(Session session, MessageProducer producer, String jsonMessage) throws JMSException {
        TextMessage message = session.createTextMessage(jsonMessage);
        producer.send(message);
        System.out.println(connector.getQuery() + "message sent to topic " + topic);
    }

    private static void closeResources(MessageProducer producer, Session session, Connection connection) {
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