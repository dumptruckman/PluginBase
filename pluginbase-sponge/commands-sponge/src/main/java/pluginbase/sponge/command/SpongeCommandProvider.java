package pluginbase.sponge.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.AbstractCommandProvider;
import pluginbase.command.CommandHandler;
import pluginbase.command.QueuedCommand;

import java.io.File;

public class SpongeCommandProvider<P> extends AbstractCommandProvider<P> {

    private final String pluginName;
    private final File dataFolder;
    private final SpongeCommandHandler commandHandler;

    public static <P> SpongeCommandProvider<P> getSpongeCommandProvider(P plugin, String pluginName, String commandPrefix, File dataFolder) {
        return new SpongeCommandProvider<P>(plugin, commandPrefix, true, pluginName, dataFolder);
    }

    public static <P> SpongeCommandProvider<P> getSpongeCommandProviderNoQueuedCommands(P plugin, String pluginName, String commandPrefix, File dataFolder) {
        return new SpongeCommandProvider<P>(plugin, commandPrefix, false, pluginName, dataFolder);
    }

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
