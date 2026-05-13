import org.junit.jupiter.api.Test;
import viper.sentinox.control.EventStoreSubscriber;

import static org.junit.jupiter.api.Assertions.*;

class EventStoreSubscriberTest {

    @Test
    void constructor_createsSubscriber() {
        EventStoreSubscriber subscriber = new EventStoreSubscriber(
                "tcp://localhost:61616",
                "TestClient"
        );

        assertNotNull(subscriber);
    }
}