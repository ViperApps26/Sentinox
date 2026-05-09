package viper.sentinox.control.subscriber;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface EventSubscriber {
    void subscribe(String topicName, String subscriptionName, MessageListener listener) throws JMSException;
}