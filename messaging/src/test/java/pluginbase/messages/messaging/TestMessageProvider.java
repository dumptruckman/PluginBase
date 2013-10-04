package pluginbase.messages.messaging;

import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import org.jetbrains.annotations.NotNull;

public class TestMessageProvider implements MessageProvider {

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args) {
        return String.format(key.getDefault(), args);
    }

    @NotNull
    @Override
    public PluginLogger getLog() {
        return Logging.getLogger();
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final String key, @NotNull final Object... args) {
        return "";
    }
}
