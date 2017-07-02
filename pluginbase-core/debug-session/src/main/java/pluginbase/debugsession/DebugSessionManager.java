package pluginbase.debugsession;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.messages.messaging.Messaging;

import java.util.HashMap;
import java.util.Map;

public class DebugSessionManager {

    public static final Message DEBUG_SESSION_REMINDER = Messages.createMessage("debugsession.reminder",
            Theme.INFO + "Your debug session is still running. Don't forget to stop it!");

    @NotNull
    private final Messaging messagingPlugin;
    @NotNull
    final Map<MessageReceiver, DebugSession> debugSessions = new HashMap<>();
    @NotNull
    private final DebugStream systemOutDebugStream;
    @NotNull
    private final DebugStream systemErrDebugStream;

    private boolean closed = false;

    public DebugSessionManager(@NotNull Messaging messagingPlugin) {
        this.messagingPlugin = messagingPlugin;
        systemOutDebugStream = new DebugStream("System.out", System.out);
        System.setOut(systemOutDebugStream);
        systemErrDebugStream = new DebugStream("System.err", System.err);
        System.setErr(systemErrDebugStream);
    }

    @Nullable
    public DebugSession getDebugSession(@NotNull MessageReceiver messageReceiver) {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        return debugSessions.get(messageReceiver);
    }

    public boolean hasDebugSession(@NotNull MessageReceiver messageReceiver) {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        return debugSessions.containsKey(messageReceiver);
    }

    @NotNull
    public DebugSession startDebugSession(@NotNull MessageReceiver messageReceiver) throws IllegalStateException {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        if (debugSessions.containsKey(messageReceiver)) {
            throw new IllegalStateException("DebugSession already exists for this messageReceiver");
        }
        DebugSubscription debugSubscription = new DebugSubscription(messageReceiver);
        DebugSession debugSession = startSession(debugSubscription);
        debugSessions.put(messageReceiver, debugSession);
        return debugSession;
    }

    @NotNull
    private DebugSession startSession(@NotNull DebugSubscription debugSubscription) {
        messagingPlugin.getMessager().subscribeToDebugBroadcast(debugSubscription);
        messagingPlugin.getLog().subscribeToDebugBroadcast(debugSubscription);
        systemOutDebugStream.subscribeToDebugBroadcast(debugSubscription);
        systemErrDebugStream.subscribeToDebugBroadcast(debugSubscription);
        Runnable reminderTask = new Runnable() {
            @Override
            public void run() {
                messagingPlugin.getMessager().message(debugSubscription.getSubscriber(), DEBUG_SESSION_REMINDER);
            }
        };
        return new DebugSession(debugSubscription, reminderTask);
    }

    @NotNull
    public DebugSession stopDebugSession(@NotNull MessageReceiver messageReceiver) throws IllegalArgumentException {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        DebugSession debugSession = getDebugSession(messageReceiver);
        if (debugSession == null) {
            throw new IllegalArgumentException("There is no DebugSession to stop for this messageReceiver");
        }
        stopSession(debugSession);
        debugSessions.remove(messageReceiver);
        return debugSession;
    }

    private void stopSession(@NotNull DebugSession debugSession) {
        messagingPlugin.getMessager().unsubscribeFromDebugBroadcast(debugSession.getDebugSubscription());
        messagingPlugin.getLog().unsubscribeFromDebugBroadcast(debugSession.getDebugSubscription());
        systemOutDebugStream.unsubscribeFromDebugBroadcast(debugSession.getDebugSubscription());
        systemErrDebugStream.unsubscribeFromDebugBroadcast(debugSession.getDebugSubscription());
        debugSession.close();
    }

    public void shutdown() {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        for (DebugSession session : debugSessions.values()) {
            stopSession(session);
        }
        debugSessions.clear();
        System.setOut(systemOutDebugStream.getOriginalStream());
        System.setErr(systemErrDebugStream.getOriginalStream());
        closed = true;
    }

    public boolean hasActiveDebugSessions() {
        if (closed) {
            throw new IllegalStateException("This debug session manager is closed. A new one must be created.");
        }
        return !debugSessions.isEmpty();
    }
}
