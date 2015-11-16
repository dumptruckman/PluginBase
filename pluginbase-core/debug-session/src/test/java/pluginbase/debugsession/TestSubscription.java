package pluginbase.debugsession;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.messaging.MessageReceiver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class TestSubscription extends DebugSubscription {

    public TestSubscription(@NotNull MessageReceiver messageReceiver) {
        super(messageReceiver);
    }

    public void assertSameLastMessage(@NotNull String expectedMessage) {
        assertFalse(recordedMessages.isEmpty());
        String actualMessage = recordedMessages.get(recordedMessages.size() - 1);
        assertEquals(expectedMessage, actualMessage);
    }

    public void assertNotSameLastMessage(@NotNull String expectedMessage) {
        if (recordedMessages.isEmpty()) {
            return;
        }
        String actualMessage = recordedMessages.get(recordedMessages.size() - 1);
        assertNotSame(expectedMessage, actualMessage);
    }
}
