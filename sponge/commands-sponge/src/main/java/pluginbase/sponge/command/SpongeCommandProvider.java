package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.command.QueuedCommand;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;

import java.io.File;
import java.util.Locale;

public class SpongeCommandProvider<O> implements CommandProvider<O> {

    private final String commandPrefix;

    @NotNull
    @Override
    public String getCommandPrefix() {
        return null;
    }

    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return null;
    }

    @Override
    public void scheduleQueuedCommandExpiration(@NotNull QueuedCommand queuedCommand) {

    }

    @Override
    public boolean useQueuedCommands() {
        return false;
    }

    @NotNull
    @Override
    public String[] getAdditionalCommandAliases(@NotNull Class<? extends Command> commandClass) {
        return new String[0];
    }

    @Override
    public void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {

    }

    @Override
    public O getPlugin() {
        return null;
    }

    @NotNull
    @Override
    public PluginLogger getLog() {
        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return null;
    }

    @NotNull
    @Override
    public Messager getMessager() {
        return null;
    }

    @Override
    public void loadMessages(@NotNull File languageFile, @NotNull Locale locale) {

    }
}
