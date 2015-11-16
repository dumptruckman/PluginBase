package pluginbase.messages.messaging;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

class MessagerDebugBroadcast {

    final Set<MessagerDebugSubscription> subscriptions = new HashSet<>();

    boolean acceptSubscription(@NotNull MessagerDebugSubscription subscription) {
        if (!subscriptions.contains(subscription)) {
            subscriptions.add(subscription);
            return true;
        } else {
            return false;
        }
    }

    boolean cancelSubscription(@NotNull MessagerDebugSubscription subscription) {
        if (subscriptions.contains(subscription)) {
            subscriptions.remove(subscription);
            return true;
        } else {
            return false;
        }
    }

    boolean hasSubscription(@NotNull MessagerDebugSubscription subscription) {
        return subscriptions.contains(subscription);
    }

    void messageRecord(@NotNull String message) {
        for (MessagerDebugSubscription subscription : subscriptions) {
            subscription.messageRecord(message);
        }
    }
}
