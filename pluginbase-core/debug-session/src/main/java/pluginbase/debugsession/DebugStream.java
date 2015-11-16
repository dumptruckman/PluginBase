package pluginbase.debugsession;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

class DebugStream extends PrintStream {

    @NotNull
    static final String DEBUG_PREFIX = "[%s]";
    @NotNull
    final Set<DebugSubscription> debugSubscriptions = new HashSet<>();
    @NotNull
    private final String streamName;
    @NotNull
    private final PrintStream originalStream;

    public DebugStream(@NotNull String streamName, @NotNull PrintStream out) {
        super(out, true);
        this.streamName = streamName;
        this.originalStream = out;
    }

    @Override
    public void print(String s) {
        super.print(s);
        for (DebugSubscription subscription : debugSubscriptions) {
            subscription.messageRecord(String.format(DEBUG_PREFIX, streamName) + s);
        }
    }

    boolean subscribeToDebugBroadcast(@NotNull DebugSubscription subscription) {
        synchronized (debugSubscriptions) {
            if (!debugSubscriptions.contains(subscription)) {
                debugSubscriptions.add(subscription);
                return true;
            }
            return false;
        }
    }

    boolean unsubscribeFromDebugBroadcast(@NotNull DebugSubscription subscription) {
        synchronized (debugSubscriptions) {
            if (debugSubscriptions.contains(subscription)) {
                debugSubscriptions.remove(subscription);
                return true;
            }
            return false;
        }
    }

    boolean hasDebugBroadcastSubscription(@NotNull DebugSubscription subscription) {
        synchronized (debugSubscriptions) {
            return debugSubscriptions.contains(subscription);
        }
    }

    PrintStream getOriginalStream() {
        return originalStream;
    }
}
