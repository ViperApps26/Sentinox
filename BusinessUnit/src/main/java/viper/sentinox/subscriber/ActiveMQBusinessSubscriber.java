package viper.sentinox.subscriber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.model.MedicineDataMart;
import viper.sentinox.view.ConsoleView;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class ActiveMQBusinessSubscriber {

    private final String brokerUrl;
    private final MedicineDataMart dataMart;
    private final ConsoleView view;
    private final Gson gson;

    public ActiveMQBusinessSubscriber(String brokerUrl,
                                      MedicineDataMart dataMart,
                                      ConsoleView view) {
        this.brokerUrl = brokerUrl;
        this.dataMart = dataMart;
        this.view = view;
        this.gson = new Gson();
    }

    public void start() {
        while (true) {
            Connection connection = null;
            Session session = null;

            try {
                connection = createConnection();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                subscribe(session, "BlueskyPosts");
                subscribe(session, "PubChemReactions");

                view.show("Business Unit is consuming events in real time.");
                keepAlive();

            } catch (Exception e) {
                view.showError("ActiveMQ connection failed. Trying to reconnect...", e);
                waitBeforeReconnect();
            } finally {
                closeSession(session);
                closeConnection(connection);
            }
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID("BusinessUnit");
        connection.start();
        return connection;
    }

    private void subscribe(Session session, String topicName) throws JMSException {
        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createDurableSubscriber(
                topic,
                "BusinessUnitSubscription_" + topicName
        );

        consumer.setMessageListener(message -> handleMessage(topicName, message));

        view.show("Subscribed to topic: " + topicName);
    }

    private void handleMessage(String topicName, Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                JsonObject event = gson.fromJson(textMessage.getText(), JsonObject.class);
                registerEvent(topicName, event);
                view.show("Event registered from topic: " + topicName);
            }
        } catch (Exception e) {
            view.showError("Error handling message from topic: " + topicName, e);
        }
    }

    private void registerEvent(String topicName, JsonObject event) {
        if ("BlueskyPosts".equals(topicName)) {
            dataMart.registerBlueskyEvent(event);
        } else if ("PubChemReactions".equals(topicName)) {
            dataMart.registerPubChemEvent(event);
        } else {
            view.show("Unknown topic: " + topicName);
        }
    }

    private void keepAlive() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void waitBeforeReconnect() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            view.showError("Reconnect interrupted", e);
        }
    }

    private void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                view.showError("Error closing session", e);
            }
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                view.showError("Error closing connection", e);
            }
        }
    }
}