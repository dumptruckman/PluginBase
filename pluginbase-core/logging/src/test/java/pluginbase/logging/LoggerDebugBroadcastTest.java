package pluginbase.logging;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggerDebugBroadcastTest {

    private TestSubscription subscription;
    private PluginLogger logging;

    @Before
    public void setup() throws Exception {
        subscription = new TestSubscription();
        LoggablePlugin plugin = mock(LoggablePlugin.class);
        when(plugin.getName()).thenReturn("TestPlugin");
        logging = PluginLogger.getLogger(plugin);
    }

    @Test
    public void testNullArgs() {
        try {
            logging.subscribeToDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            logging.unsubscribeFromDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            logging.hasDebugBroadcastSubscription(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
    }

    @Test
    public void testAcceptSubscription() {
        assertTrue(logging.subscribeToDebugBroadcast(subscription));
        assertTrue(logging.hasDebugBroadcastSubscription(subscription));
        assertFalse(logging.subscribeToDebugBroadcast(subscription));
    }

    @Test
    public void testCancelSubscription() {
        assertFalse(logging.unsubscribeFromDebugBroadcast(subscription));
        assertTrue(logging.subscribeToDebugBroadcast(subscription));
        assertTrue(logging.unsubscribeFromDebugBroadcast(subscription));
    }

    @Test
    public void testRecordMessageToBroadcast() {
        logging.subscribeToDebugBroadcast(subscription);
        logging.log(Level.INFO, "Test");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[INFO]" + logging.getPrefixedMessage("Test"));
        logging.log(Level.WARNING, "Test");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[WARNING]" + logging.getPrefixedMessage("Test"));
        logging.log(Level.SEVERE, "Test");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[SEVERE]" + logging.getPrefixedMessage("Test"));
        logging.log(Level.FINE, "Test1");
        subscription.assertNotSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[INFO]" + logging.getDebugString("Test1"));
        logging.setDebugLevel(3);
        logging.log(Level.FINE, "Test1");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[INFO]" + logging.getDebugString("Test1"));
        logging.log(Level.FINER, "Test2");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[INFO]" + logging.getDebugString("Test2"));
        logging.log(Level.FINEST, "Test3");
        subscription.assertSameLastMessage(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, logging) + "[INFO]" + logging.getDebugString("Test3"));

    }
}
