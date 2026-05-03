package viper.sentinox.subscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.view.BusinessUnitView;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class ActiveMQEventSubscriber implements EventSubscriber {

    private final String brokerUrl;
    private final String clientId;
    private final String subscriptionName;
    private final EventHandler eventHandler;
    private final BusinessUnitView view;

    public ActiveMQEventSubscriber(String brokerUrl,
                                   String clientId,
                                   String subscriptionName,
                                   EventHandler eventHandler,
                                   BusinessUnitView view) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
        this.subscriptionName = subscriptionName;
        this.eventHandler = eventHandler;
        this.view = view;
    }

    @Override
    public void subscribe() {
        try {
            Connection connection = createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            subscribeToTopic(session, "BlueskyPosts", "Bluesky");
            subscribeToTopic(session, "PubChemReactions", "PubChem");

        } catch (Exception e) {
            view.showError("Error subscribing to ActiveMQ topics", e);
        }
    }

    private Connection createConnection() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID(clientId);
        connection.start();
        return connection;
    }

    private void subscribeToTopic(Session session, String topicName, String suffix)
            throws JMSException {

        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createDurableSubscriber(
                topic,
                subscriptionName + "_" + suffix
        );

        consumer.setMessageListener(message -> {
            try {
                if (message instanceof TextMessage textMessage) {
                    eventHandler.handle(topicName, textMessage.getText());
                }
            } catch (Exception e) {
                view.showError("Error handling message from " + topicName, e);
            }
        });

        view.showMessage("Subscribed to topic: " + topicName);
    }
}