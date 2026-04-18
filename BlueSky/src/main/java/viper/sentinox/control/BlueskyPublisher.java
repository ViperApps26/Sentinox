package viper.sentinox.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.model.BlueskyConnect;
import viper.sentinox.model.BlueskyEvent;
import viper.sentinox.model.BlueskyGet;
import viper.sentinox.model.SentimentAnalysis;

import javax.jms.*;
import java.io.IOException;
import java.util.List;

public class BlueskyPublisher implements BlueskyPublisherInterface{

    private final BlueskyConnect connect;
    private final BlueskyGet get;
    private final SentimentAnalysis sentiment;
    private final Gson gson;
    private final String brokerUrl;
    private final String topicName;

    public BlueskyPublisher(BlueskyConnect connect,
                            BlueskyGet get,
                            SentimentAnalysis sentiment) {
        this.connect = connect;
        this.get = get;
        this.sentiment = sentiment;
        this.gson = new Gson();
        this.brokerUrl = "tcp://localhost:61616";
        this.topicName = "BlueskyPosts";
    }

    public void publishPosts(String token) throws IOException {
        JsonArray postAttributes = get.getPostsAttributes(token);

        List<String> authors = get.getAuthors(postAttributes);
        List<String> posts = get.getPosts(postAttributes);
        List<String> creationDates = get.getCreationDates(postAttributes);

        if (posts.isEmpty()) {
            System.out.println(connect.getQuery() + " has no posts to publish");
        } else {
            publishEachEvent(
                    connect.getQuery(),
                    authors,
                    posts,
                    creationDates
            );
            System.out.println(connect.getQuery() + " posts published correctly");
        }
    }

    private void publishEachEvent(String medicine,
                               List<String> authors,
                               List<String> posts,
                               List<String> creationDates) {

        for (int i = 0; i < posts.size(); i++) {
            String text = posts.get(i);
            String sentiment = this.sentiment.analyze(text).getOverall();

            BlueskyEvent event = new BlueskyEvent(
                    System.currentTimeMillis(),
                    "BlueskyFeeder",
                    medicine,
                    authors.get(i),
                    text,
                    sentiment,
                    creationDates.get(i)
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
        return session.createProducer(destination);
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