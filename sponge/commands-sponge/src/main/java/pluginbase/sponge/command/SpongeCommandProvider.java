package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.AbstractCommandProvider;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.command.QueuedCommand;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SpongeCommandProvider<P> extends AbstractCommandProvider<P> {

    private final String pluginName;
    private final File dataFolder;
    private final SpongeCommandHandler commandHandler;

    private SpongeCommandProvider(P plugin, String commandPrefix, boolean useQueuedCommands,
                                  String pluginName, File dataFolder) {
        super(plugin, commandPrefix, useQueuedCommands);
        this.pluginName = pluginName;
        this.dataFolder = dataFolder;
        this.commandHandler = new SpongeCommandHandler(this);
    }

    @NotNull
    @Override
    public String getName() {
        return pluginName;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @Override
    public void scheduleQueuedCommandExpiration(@NotNull QueuedCommand queuedCommand) {

    }
}
