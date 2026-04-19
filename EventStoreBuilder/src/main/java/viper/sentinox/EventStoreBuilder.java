package viper.sentinox;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EventStoreBuilder implements EventStoreBuilderInterface {

    private final String brokerUrl;
    private final String clientId;
    private final String subscriptionName;
    private final String baseDir;

    private final Gson gson = new Gson();
    private final DateTimeFormatter dateFormatter
            = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of("UTC"));

    public EventStoreBuilder(){
        brokerUrl = "tcp://localhost:61616";
        clientId = "EventStoreBuilder";
        subscriptionName = "EventStoreSubscription";
        baseDir = "eventstore";
    }

    public void store() {
        Connection connection = null;
        Session session = null;
        try {
            connection = createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("Event Store Builder listening on:");
            subscribe(session, "PubChemReactions", "PubChem");
            subscribe(session, "BlueskyPosts", "Bluesky");

            System.out.println("Event Store Builder is running...");
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println("Error in Event Store Builder");
            e.printStackTrace();
        } finally {
            closeSession(session);
            closeConnection(connection);
        }
    }

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = factory.createConnection();
        connection.setClientID(clientId);
        connection.start();
        return connection;
    }

    private void subscribe(Session session, String topicName, String subscriptionSuffix) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(
                topic,
                subscriptionName + "_" + subscriptionSuffix
        );
        System.out.println(" - " + topic.getTopicName());
        getEvents(topicName, consumer, topic);
    }

    private void getEvents(String topicName, MessageConsumer consumer, Topic topic) throws JMSException {
        consumer.setMessageListener(message -> {
            try {
                if (message instanceof TextMessage textMessage) {
                    String json = textMessage.getText();
                    handleEvent(json, topic.getTopicName());
                }
            } catch (Exception e) {
                System.out.println("Error handling event for topic " + topicName);
                e.printStackTrace();
            }
        });
    }

    private void handleEvent(String json, String topicName) {
        try {
            JsonObject event = gson.fromJson(json, JsonObject.class);

            String ss = event.get("ss").getAsString();
            String date = dateFormatter.format(Instant.ofEpochMilli(event.get("ts").getAsLong()));

            File file = resolveEventFile(topicName, ss, date);
            writeEventToFile(file, json);

        } catch (Exception e) {
            System.out.println("Error handling event: " + e.getMessage());
        }
    }

    private File resolveEventFile(String topicName, String ss, String date) {
        File path = new File(baseDir + File.separator + topicName + File.separator + ss);
        if (!path.exists() && !path.mkdirs()) {
            System.out.println("Warning: could not create directory " + path.getAbsolutePath());
        }
        return new File(path, date + ".events");
    }

    private void writeEventToFile(File file, String json) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(json);
            fw.write("\n");
            System.out.println("Event stored in: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing event to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException ignored) {}
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ignored) {}
        }
    }
}

