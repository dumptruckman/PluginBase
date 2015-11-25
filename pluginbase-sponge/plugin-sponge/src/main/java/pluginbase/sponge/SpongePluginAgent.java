package pluginbase.sponge;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import pluginbase.jdbc.DatabaseSettings;
import pluginbase.messages.messaging.MessagerFactory;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginAgent;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.sponge.command.SpongeCommandProvider;
import pluginbase.sponge.messaging.SpongeMessagerProvider;
import pluginbase.sponge.minecraft.SpongeTools;

import java.io.File;

public class SpongePluginAgent<P> extends PluginAgent<P> {

    static {
        PermFactory.useBasicPermissionFactory();
        MessagerFactory.registerMessagerProvider(new SpongeMessagerProvider());
    }

    private final PluginContainer pluginContainer;
    private final PluginInfo pluginInfo;
    private final ServerInterface serverInterface;
    private final File dataFolder;

    @Nullable
    private CommentedConfigurationNode config;

    @NotNull
    public static <P> SpongePluginAgent<P> getPluginAgent(@NotNull Game game, @NotNull Class<P> pluginInterface,
                                                          @NotNull P plugin, @NotNull PluginContainer pluginContainer,
                                                          @NotNull String commandPrefix, @NotNull File dataFolder) {
        SpongeTools.registerGameInstance(game);
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new SpongePluginAgent<P>(pluginInterface, plugin, pluginContainer, commandPrefix, dataFolder, true);
    }

    @NotNull
    public static <P> SpongePluginAgent<P> getPluginAgentNoQueuedCommands(@NotNull Game game, @NotNull Class<P> pluginInterface,
                                                                          @NotNull P plugin, @NotNull PluginContainer pluginContainer,
                                                                          @NotNull String commandPrefix, @NotNull File dataFolder) {
        SpongeTools.registerGameInstance(game);
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new SpongePluginAgent<P>(pluginInterface, plugin, pluginContainer, commandPrefix, dataFolder, false);
    }

    private SpongePluginAgent(@NotNull Class<P> pluginInterface, @NotNull P plugin, @NotNull PluginContainer pluginContainer, @NotNull String commandPrefix, @NotNull File dataFolder, boolean queuedCommands) {
        super(pluginInterface, (P) plugin, queuedCommands ?
                SpongeCommandProvider.getSpongeCommandProvider(plugin, pluginContainer.getName(), commandPrefix, dataFolder)
                : SpongeCommandProvider.getSpongeCommandProviderNoQueuedCommands(plugin, pluginContainer.getName(), commandPrefix, dataFolder));
        this.pluginContainer = pluginContainer;
        this.pluginInfo = new SpongePluginInfo(pluginContainer);
        this.serverInterface = new SpongeServerInterface();
        this.dataFolder = dataFolder;
    }

    @NotNull
    @Override
    protected PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @NotNull
    @Override
    protected File getDataFolder() {
        return dataFolder;
    }

    @Override
    protected void disablePlugin() {
        // TODO
    }

    @Override
    protected ServerInterface getServerInterface() {
        return serverInterface;
    }
}
