package pluginbase.messages.messaging;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class MessagerDebugBroadcastTest {

    private MessagerDebugSubscription subscriber;
    private MessageReceiver messageReceiver;
    private MessagerDebugBroadcast broadcaster;

    @Before
    public void setup() throws Exception {
        messageReceiver = mock(MessageReceiver.class);
        subscriber = new TestSubscription(messageReceiver);
        broadcaster = new MessagerDebugBroadcast();
    }

    @Test
    public void testNullArgs() {
        try {
            broadcaster.acceptSubscription(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            broadcaster.cancelSubscription(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            broadcaster.hasSubscription(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
    }

    @Test
    public void testAcceptSubscription() {
        assertTrue(broadcaster.acceptSubscription(subscriber));
        assertTrue(broadcaster.hasSubscription(subscriber));
        assertFalse(broadcaster.acceptSubscription(subscriber));
    }

    @Test
    public void testCancelSubscription() {
        assertFalse(broadcaster.cancelSubscription(subscriber));
        assertTrue(broadcaster.acceptSubscription(subscriber));
        assertTrue(broadcaster.cancelSubscription(subscriber));
    }
}
