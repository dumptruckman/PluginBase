package pluginbase.messages.messaging;

import org.jetbrains.annotations.NotNull;
import pluginbase.logging.DebugSubscription;

/**
 * This class can be implemented in order to listen in on messages sent to a {@link MessageReceiver}.
 * <br>
 * Subscribe to the debug broadcast for a {@link MessageReceiver} by calling
 * {@link Messager#subscribeToDebugBroadcast(MessagerDebugSubscription)}.
 */
public interface MessagerDebugSubscription extends DebugSubscription {

    @NotNull
    MessageReceiver getSubscriber();
}
