package pluginbase.debugsession;

import org.junit.Before;
import org.junit.Test;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.TestMessagingPlugin;

import java.util.List;

import static org.mockito.Mockito.mock;
import static junit.framework.Assert.*;

public class DebugSessionTest {

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
    public void testGetCompiledOutput() throws Exception {
        DebugSession session = debugSessionManager.startDebugSession(messageReceiver);
        assertNotNull(session);
        plugin.getMessager().message(messageReceiver, "This is a test");
        plugin.getLog().info("This is a test");
        System.out.println("This is a test");
        System.err.println("This is a test");

        try {
            session.getCompiledOutput();
            fail();
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().equals("Debug session must be closed to get compiled output."));
        }

        debugSessionManager.stopDebugSession(messageReceiver);
        assertTrue(session.isClosed());
        plugin.getMessager().message(messageReceiver, "This should not record");
        plugin.getLog().info("This should not record");
        System.out.println("This should not record");
        System.err.println("This should not record");

        List<String> output = session.getCompiledOutput();
        assertEquals(4, output.size());
        assertEquals(String.format(Messager.DEBUG_BROADCAST_PREFIX, messageReceiver) + "This is a test", output.get(0));
        assertEquals(String.format(PluginLogger.DEBUG_BROADCAST_PREFIX, plugin.getLog()) + "[INFO]" + plugin.getLog().getPrefixedMessage("This is a test"), output.get(1));
        assertEquals(String.format(DebugStream.DEBUG_PREFIX, "System.out") + "This is a test", output.get(2));
        assertEquals(String.format(DebugStream.DEBUG_PREFIX, "System.err") + "This is a test", output.get(3));
    }
}
