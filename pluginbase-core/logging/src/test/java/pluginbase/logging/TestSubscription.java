package pluginbase.logging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class TestSubscription implements DebugSubscription {

    private final List<String> recordedMessages = new ArrayList<>();

    @Override
    public void messageRecord(@NotNull String message) {
        recordedMessages.add(message);
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

    @Override
    public List<String> getRecordedMessages() {
        return recordedMessages;
    }
}
