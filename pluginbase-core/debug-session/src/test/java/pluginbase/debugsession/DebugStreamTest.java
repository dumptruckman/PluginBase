package pluginbase.debugsession;

import org.junit.Before;
import org.junit.Test;
import pluginbase.messages.messaging.MessageReceiver;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class DebugStreamTest {

    private TestSubscription subscription;
    private DebugStream systemOut;
    private DebugStream systemErr;

    @Before
    public void setup() throws Exception {
        MessageReceiver messageReceiver = mock(MessageReceiver.class);
        subscription = new TestSubscription(messageReceiver);
        systemOut = new DebugStream("System.out", System.out);
        System.setOut(systemOut);
        systemErr = new DebugStream("System.err", System.err);
        System.setErr(systemErr);
    }

    @Test
    public void testNullArgs() {
        try {
            systemOut.subscribeToDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            systemOut.unsubscribeFromDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            systemOut.hasDebugBroadcastSubscription(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
    }

    @Test
    public void testAcceptSubscription() {
        assertTrue(systemOut.subscribeToDebugBroadcast(subscription));
        assertTrue(systemOut.hasDebugBroadcastSubscription(subscription));
        assertFalse(systemOut.subscribeToDebugBroadcast(subscription));
    }

    @Test
    public void testCancelSubscription() {
        assertFalse(systemOut.unsubscribeFromDebugBroadcast(subscription));
        assertTrue(systemOut.subscribeToDebugBroadcast(subscription));
        assertTrue(systemOut.unsubscribeFromDebugBroadcast(subscription));
    }

    @Test
    public void testRecordMessageToBroadcast() {
        subscription.assertNotSameLastMessage(String.format(DebugStream.DEBUG_PREFIX, "System.out") + "Test");
        subscription.assertNotSameLastMessage(String.format(DebugStream.DEBUG_PREFIX, "System.err") + "Test");
        systemOut.subscribeToDebugBroadcast(subscription);
        systemErr.subscribeToDebugBroadcast(subscription);
        System.out.println("Test");
        subscription.assertSameLastMessage(String.format(DebugStream.DEBUG_PREFIX, "System.out") + "Test");
        System.err.println("Test");
        subscription.assertSameLastMessage(String.format(DebugStream.DEBUG_PREFIX, "System.err") + "Test");
    }
}
