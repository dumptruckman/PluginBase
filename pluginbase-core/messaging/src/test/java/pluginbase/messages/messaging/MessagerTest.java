package pluginbase.messages.messaging;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MessagerTest {

    private Messager messager;
    private MessagerDebugSubscription subscription;
    private MessageReceiver messageReceiver;

    @Before
    public void setup() throws Exception {
        messager = new TestMessagingPlugin().getMessager();
        messageReceiver = mock(MessageReceiver.class);
        subscription = new TestSubscription(messageReceiver);
    }

    @Test
    public void testNullArgs() {
        try {
            messager.getDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            messager.subscribeToDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            messager.unsubscribeFromDebugBroadcast(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
    }

    @Test
    public void testSubscribeToNonexistentBroadcastNewBroadcastIsCreated() {
        assertNull(messager.getDebugBroadcast(messageReceiver));
        assertTrue(messager.subscribeToDebugBroadcast(subscription));
        assertNotNull(messager.getDebugBroadcast(messageReceiver));
    }

    @Test
    public void testSubscribeToExistingBroadcastNewBroadcastIsNotCreated() {
        MessagerDebugBroadcast broadcaster = new MessagerDebugBroadcast();
        messager.debugBroadcastMap.put(messageReceiver, broadcaster);
        assertTrue(messager.subscribeToDebugBroadcast(subscription));
        assertSame(broadcaster, messager.getDebugBroadcast(messageReceiver));
    }

    @Test
    public void testSubscribeToBroadcastWhenAlreadySubscribed() {
        assertTrue(messager.subscribeToDebugBroadcast(subscription));
        MessagerDebugBroadcast broadcaster = messager.getDebugBroadcast(messageReceiver);
        assertNotNull(broadcaster);
        assertTrue(broadcaster.hasSubscription(subscription));
        assertFalse(messager.subscribeToDebugBroadcast(subscription));
    }

    @Test
    public void testUnsubscribeToNonExistentBroadcast() {
        assertFalse(messager.unsubscribeFromDebugBroadcast(subscription));
        assertNull(messager.getDebugBroadcast(messageReceiver));
    }

    @Test
    public void testUnsubscribeToNonExistingBroadcastAndSubscription() {
        assertTrue(messager.subscribeToDebugBroadcast(subscription));
        MessagerDebugBroadcast broadcaster = messager.getDebugBroadcast(messageReceiver);
        assertNotNull(broadcaster);
        assertTrue(broadcaster.hasSubscription(subscription));
        assertTrue(messager.unsubscribeFromDebugBroadcast(subscription));
        assertFalse(broadcaster.hasSubscription(subscription));
        assertFalse(messager.unsubscribeFromDebugBroadcast(subscription));
    }

    @Test
    public void testRecordMessageToBroadcast() {
        messager.subscribeToDebugBroadcast(subscription);
        messager.message(messageReceiver, "Test");
        MessagerDebugBroadcast broadcast = messager.getDebugBroadcast(messageReceiver);
        assertNotNull(broadcast);
        boolean fail = true;
        for (MessagerDebugSubscription subscription : broadcast.subscriptions) {
            if (subscription instanceof TestSubscription) {
                ((TestSubscription) subscription).assertSameLastMessage(String.format(Messager.DEBUG_BROADCAST_PREFIX, messageReceiver) + "Test");
                fail = false;
            } else {
                fail("Must use TestSubscription for testing");
            }
        }
        assertFalse(fail);
    }
}
