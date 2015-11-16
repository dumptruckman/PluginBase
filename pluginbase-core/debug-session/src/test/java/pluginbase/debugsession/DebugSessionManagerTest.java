package pluginbase.debugsession;

import org.junit.Before;
import org.junit.Test;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.TestMessagingPlugin;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;

public class DebugSessionManagerTest {

    DebugSessionManager debugSessionManager;
    MessageReceiver messageReceiver;
    TestMessagingPlugin plugin;

    @Before
    public void setup() throws Exception {
        plugin = new TestMessagingPlugin();
        debugSessionManager = new DebugSessionManager(plugin);
        messageReceiver = mock(MessageReceiver.class);
    }

    @Test
    public void testNullArgs() {
        try {
            debugSessionManager.startDebugSession(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            debugSessionManager.getDebugSession(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            debugSessionManager.startDebugSession(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
        try {
            debugSessionManager.stopDebugSession(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().endsWith("must not be null"));
        }
    }

    @Test
    public void testGetDebugSessionThatDoesNotExist() {
        assertFalse(debugSessionManager.hasDebugSession(messageReceiver));
        assertNull(debugSessionManager.getDebugSession(messageReceiver));
    }

    @Test
    public void testGetDebugSessionThatExists() {
        debugSessionManager.debugSessions.put(messageReceiver, new DebugSession(new DebugSubscription(messageReceiver), null));
        assertTrue(debugSessionManager.hasDebugSession(messageReceiver));
        assertNotNull(debugSessionManager.getDebugSession(messageReceiver));
    }

    @Test
    public void testStartDebugSession() {
        DebugSession debugSession = debugSessionManager.startDebugSession(messageReceiver);
        assertNotNull(debugSession);
        assertFalse(plugin.getMessager().subscribeToDebugBroadcast(debugSession.getDebugSubscription()));
        assertFalse(plugin.getLog().subscribeToDebugBroadcast(debugSession.getDebugSubscription()));
        assertFalse(debugSession.isClosed());
    }

    @Test
    public void testStartDebugSessionWhenOneExists() {
        debugSessionManager.debugSessions.put(messageReceiver, new DebugSession(new DebugSubscription(messageReceiver), null));
        try {
            debugSessionManager.startDebugSession(messageReceiver);
            fail();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("DebugSession already exists for this messageReceiver"));
        }
    }

    @Test
    public void testStopNonExistentDebugSession() {
        try {
            debugSessionManager.stopDebugSession(messageReceiver);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("There is no DebugSession to stop for this messageReceiver"));
        }
    }

    @Test
    public void testStopDebugSession() {
        DebugSession expectedDebugSession = debugSessionManager.startDebugSession(messageReceiver);
        assertFalse(expectedDebugSession.isClosed());
        debugSessionManager.debugSessions.put(messageReceiver, expectedDebugSession);
        assertTrue(debugSessionManager.hasDebugSession(messageReceiver));
        DebugSession actualDebugSession = debugSessionManager.stopDebugSession(messageReceiver);
        assertFalse(debugSessionManager.hasDebugSession(messageReceiver));
        assertSame(expectedDebugSession, actualDebugSession);
        assertFalse(plugin.getMessager().unsubscribeFromDebugBroadcast(actualDebugSession.getDebugSubscription()));
        assertFalse(plugin.getLog().unsubscribeFromDebugBroadcast(actualDebugSession.getDebugSubscription()));
        assertTrue(actualDebugSession.isClosed());
    }
}
