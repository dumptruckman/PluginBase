import org.jetbrains.annotations.NotNull;
import pluginbase.jdbc.DatabaseSettings;
import pluginbase.messages.messaging.MessagerFactory;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.plugin.PluginAgent;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.sponge.command.SpongeCommandProvider;
import pluginbase.sponge.messaging.SpongeMessagerProvider;

import java.io.File;

public class SpongePluginAgent<P> extends PluginAgent<P> {

    static {
        MessagerFactory.registerMessagerProvider(new SpongeMessagerProvider());
    }

    @NotNull
    public static <P> SpongePluginAgent<P> getPluginAgent(@NotNull Class<P> pluginInterface, @NotNull P plugin, @NotNull String pluginName, @NotNull String commandPrefix, @NotNull File dataFolder) {
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new SpongePluginAgent<P>(pluginInterface, plugin, pluginName, commandPrefix, dataFolder, true);
    }

    @NotNull
    public static <P> SpongePluginAgent<P> getPluginAgentNoQueuedCommands(@NotNull Class<P> pluginInterface, @NotNull P plugin, @NotNull String pluginName, @NotNull String commandPrefix, @NotNull File dataFolder) {
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new SpongePluginAgent<P>(pluginInterface, plugin, pluginName, commandPrefix, dataFolder, false);
    }

    private SpongePluginAgent(@NotNull Class<P> pluginInterface, @NotNull P plugin, @NotNull String pluginName, @NotNull String commandPrefix, @NotNull File dataFolder, boolean queuedCommands) {
        super(pluginInterface, (P) plugin, queuedCommands ?
                SpongeCommandProvider.getSpongeCommandProvider(plugin, pluginName, commandPrefix, dataFolder)
                : SpongeCommandProvider.getSpongeCommandProviderNoQueuedCommands(plugin, pluginName, commandPrefix, dataFolder));
    }

    @NotNull
    @Override
    protected PluginInfo getPluginInfo() {
        return null;
    }

    @NotNull
    @Override
    protected File getDataFolder() {
        return null;
    }

    @NotNull
    @Override
    protected Settings loadSettings() {
        return null;
    }

    @NotNull
    @Override
    public DatabaseSettings loadDatabaseSettings(@NotNull DatabaseSettings defaults) {
        return null;
    }

    @Override
    protected void disablePlugin() {

    }

    @Override
    protected ServerInterface getServerInterface() {
        return null;
    }

    @Override
    protected void saveSettings() throws SendablePluginBaseException {

    }
}
