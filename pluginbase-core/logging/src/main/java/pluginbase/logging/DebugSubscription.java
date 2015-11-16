package pluginbase.logging;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DebugSubscription {

    void messageRecord(@NotNull String message);

    @NotNull
    List<String> getRecordedMessages();
}
