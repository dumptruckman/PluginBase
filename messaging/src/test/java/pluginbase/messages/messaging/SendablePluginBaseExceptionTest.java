package pluginbase.messages.messaging;

import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Localizable;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.MockGateway;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class SendablePluginBaseExceptionTest implements Localizable {

    private static final BundledMessage TEST_1 = Message.bundleMessage(Message.createMessage("test.1", "test message 1"));
    private static final BundledMessage TEST_2 = Message.bundleMessage(Message.createMessage("test.1", "test message 2"));
    private static final BundledMessage TEST_3 = Message.bundleMessage(Message.createMessage("test.1", "test message 3"));
    private static final BundledMessage TEST_4 = Message.bundleMessage(Message.createMessage("test.1", "test message 4"));

    @NotNull
    @Override
    public PluginLogger getLog() {
        return Logging.getLogger();
    }

    private Messager messager;
    private MessageReceiver receiver;

    @Before
    public void setUp() throws Exception {
        MockGateway.MOCK_STANDARD_METHODS = false;
        Messages.registerMessages(this, this.getClass());
        messager = spy(new Messager(new TestMessageProvider()));
        receiver = mock(MessageReceiver.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                System.out.println("[Receiver] " + invocation.getArguments()[0]);
                return null;
            }
        }).when(receiver).sendMessage(anyString());
    }

    @After
    public void tearDown() throws Exception {
    }

    boolean e1Sent = false, e2Sent = false, e3Sent = false, e4Sent = false;

    @Test
    public void testSendException() throws Exception {
        SendablePluginBaseException e4 = new SendablePluginBaseException(TEST_4);
        Throwable e3 = new SendablePluginBaseException(TEST_3, e4);
        PluginBaseException e2 = new SendablePluginBaseException(TEST_2, e3);
        Throwable throwable = new Throwable("intermission", e2);
        SendablePluginBaseException e1 = new SendablePluginBaseException(TEST_1, throwable);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                final BundledMessage b = (BundledMessage) invocation.getArguments()[1];
                messager.message((MessageReceiver) invocation.getArguments()[0], b.getMessage(), b.getArgs());                if (b.equals(TEST_4)) {
                    e4Sent = true;
                } else if (b.equals(TEST_3)) {
                    e3Sent = true;
                } else if (b.equals(TEST_2)) {
                    e2Sent = true;
                } else if (b.equals(TEST_1)) {
                    e1Sent = true;
                } else if (b.getMessage().equals(Messages.CAUSE_EXCEPTION)) {
                    Object o = b.getArgs()[0];
                    if (o.equals(TEST_4.getMessage().getDefault())) {
                        e4Sent = true;
                    } else if (o.equals(TEST_3.getMessage().getDefault())) {
                        e3Sent = true;
                    } else if (o.equals(TEST_2.getMessage().getDefault())) {
                        e2Sent = true;
                    } else if (o.equals(TEST_1.getMessage().getDefault())) {
                        e1Sent = true;
                    }
                }
                return null;
            }
        }).when(messager).message(any(MessageReceiver.class), any(BundledMessage.class));
        e1.sendException(messager, receiver);
        assertTrue(e1Sent);
        assertTrue(e2Sent);
        assertTrue(e3Sent);
        assertTrue(e4Sent);
    }
}
