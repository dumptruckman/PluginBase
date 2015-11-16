package pluginbase.messages.messaging;

import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.LocalizablePlugin;
import pluginbase.messages.Message;
import pluginbase.messages.MessageProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Locale;

public class TestMessagingPlugin implements MessageProvider, Messaging {

    private Messager messager = new Messager(this);

    @NotNull
    @Override
    public PluginLogger getLog() {
        return Logging.getLogger();
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
        return this;
    }

    @NotNull
    @Override
    public Messager getMessager() {
        return messager;
    }

    @Override
    public void loadMessages(@NotNull File languageFile, @NotNull Locale locale) {

    }
}
