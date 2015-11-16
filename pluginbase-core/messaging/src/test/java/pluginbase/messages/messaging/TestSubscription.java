package pluginbase.messages.messaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestSubscription implements MessagerDebugSubscription {

    @NotNull
    private final MessageReceiver receiver;

    private final List<String> recordedMessages = new ArrayList<>();

    public TestSubscription(@NotNull MessageReceiver receiver) {
        this.receiver = receiver;
    }

    @NotNull
    @Override
    public MessageReceiver getSubscriber() {
        return receiver;
    }

    @Override
    public void messageRecord(@NotNull String message) {
        recordedMessages.add(message);
    }

    public void assertSameLastMessage(@NotNull String expectedMessage) {
        assertFalse(recordedMessages.isEmpty());
        String actualMessage = recordedMessages.get(recordedMessages.size() - 1);
        assertEquals(expectedMessage, actualMessage);
    }

    @Override
    public List<String> getRecordedMessages() {
        return recordedMessages;
    }
}
