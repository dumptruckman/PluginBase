package pluginbase.debugsession;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.MessagerDebugSubscription;

import java.util.ArrayList;
import java.util.List;

class DebugSubscription implements MessagerDebugSubscription {

    @NotNull
    final List<String> recordedMessages = new ArrayList<>();
    @NotNull
    private final MessageReceiver messageReceiver;

    public DebugSubscription(@NotNull MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    @NotNull
    @Override
    public MessageReceiver getSubscriber() {
        return messageReceiver;
    }

    @Override
    public void messageRecord(@NotNull String message) {
        synchronized (recordedMessages) {
            recordedMessages.add(message);
        }
    }

    @NotNull
    @Override
    public List<String> getRecordedMessages() {
        synchronized (recordedMessages) {
            return new ArrayList<>(recordedMessages);
        }
    }
}
