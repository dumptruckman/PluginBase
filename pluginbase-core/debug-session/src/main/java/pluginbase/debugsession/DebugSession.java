package pluginbase.debugsession;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugSession {

    @NotNull
    private final DebugSubscription debugSubscription;
    private boolean closed = false;

    @Nullable
    private final Runnable reminderTask;

    private int taskId = -1;

    DebugSession(@NotNull DebugSubscription debugSubscription, @Nullable Runnable reminderTask) {
        this.debugSubscription = debugSubscription;
        this.reminderTask = reminderTask;
    }

    void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @NotNull
    DebugSubscription getDebugSubscription() {
        return debugSubscription;
    }

    public List<String> getCompiledOutput() {
        if (!isClosed()) {
            throw new IllegalStateException("Debug session must be closed to get compiled output.");
        }
        return getDebugSubscription().getRecordedMessages();
    }

    @Nullable
    public Runnable getReminderTask() {
        return reminderTask;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }
}
