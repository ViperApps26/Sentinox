package viper.sentinox.control.subscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessUnitSubscriber implements EventSubscriber {

    private final String brokerUrl;
    private final String clientId;

    private Connection connection;
    private Session session;
    private final List<MessageConsumer> consumers = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(BusinessUnitSubscriber.class);

    public BusinessUnitSubscriber(String brokerUrl,
                                  String clientId) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
    }

    public void connect() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        connection.setClientID(clientId);
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void subscribe(String topicName, String subscriptionName, MessageListener listener) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(
                topic,
                subscriptionName
        );

        consumer.setMessageListener(listener);

        consumers.add(consumer);
        log.info("Subscribed to topic: {}", topicName);
    }

    public void waitForever() throws InterruptedException {
        log.info("Business Unit Subscriber running...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public void close() {
        try {
            for (MessageConsumer c : consumers) c.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (Exception ignored) {}
    }
}