package viper.sentinox.control.store;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.activemq.ActiveMQConnectionFactory;
import viper.sentinox.control.BlueskyConnector;
import viper.sentinox.model.BlueskyEvent;
import viper.sentinox.control.BlueskyGet;
import viper.sentinox.control.SentimentAnalysis;

import javax.jms.*;
import java.io.IOException;
import java.util.List;

public class ActiveMQBlueskyStore implements BlueskyStore {

    private final BlueskyConnector connector;
    private final BlueskyGet get;
    private final SentimentAnalysis sentiment;
    private final Gson gson;

    public ActiveMQBlueskyStore(BlueskyConnector connector,
                                BlueskyGet get,
                                SentimentAnalysis sentiment) {
        this.connector = connector;
        this.get = get;
        this.sentiment = sentiment;
        this.gson = new Gson();
    }

    public void publishPosts(String token, String topic, String url) throws IOException {
        JsonArray postAttributes = get.getPostsAttributes(token);

        List<String> authors = get.getAuthors(postAttributes);
        List<String> posts = get.getPosts(postAttributes);
        List<String> creationDates = get.getCreationDates(postAttributes);

        if (posts.isEmpty()) {
            System.out.println(connector.getQuery() + " has no posts to publish");
        } else {
            publishEachEvent(
                    connector.getQuery(),
                    authors,
                    posts,
                    creationDates,
                    topic,
                    url
            );
            System.out.println(connector.getQuery() + " posts published correctly");
        }
    }

    private void publishEachEvent(String medicine,
                                  List<String> authors,
                                  List<String> posts,
                                  List<String> creationDates,
                                  String topic,
                                  String url) {

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
            publish(gson.toJson(event), topic, url);
        }
    }

    private void publish(String jsonMessage, String topic, String url) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = createConnection(url);
            session = createSession(connection);
            producer = createProducer(session, topic);

            sendMessage(session, producer, jsonMessage, topic);
        } catch (Exception e) {
            System.out.println("Error publishing message to ActiveMQ");
        } finally {
            closeResources(producer, session, connection);
        }
    }

    private Connection createConnection(String url) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    private Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    private MessageProducer createProducer(Session session, String topic) throws JMSException {
        Destination destination = session.createTopic(topic);
        return session.createProducer(destination);
    }

    private void sendMessage(Session session, MessageProducer producer, String jsonMessage, String topic) throws JMSException {
        TextMessage message = session.createTextMessage(jsonMessage);
        producer.send(message);
        System.out.println("Message sent to topic " + topic);
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

    @Override
    public void save(BlueskyEvent event) {

    }
}