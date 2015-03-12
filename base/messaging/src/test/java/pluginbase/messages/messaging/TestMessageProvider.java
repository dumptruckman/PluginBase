package pluginbase.messages.messaging;

import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.LocalizablePlugin;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import org.jetbrains.annotations.NotNull;

public class TestMessageProvider implements MessageProvider {

    private LocalizablePlugin plugin;

    public TestMessageProvider(LocalizablePlugin plugin) {
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final Message key, @NotNull final Object... args) {
        return String.format(key.getDefault(), args);
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@NotNull final String key, @NotNull final Object... args) {
        return "";
    }

    @NotNull
    @Override
    public LocalizablePlugin getPlugin() {
        return plugin;
    }
}
