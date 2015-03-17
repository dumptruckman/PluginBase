package pluginbase.sponge;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;
import pluginbase.jdbc.DatabaseSettings;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.MessagerFactory;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.plugin.PluginAgent;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.sponge.command.SpongeCommandProvider;
import pluginbase.sponge.config.HoconSerializableConfigLoader;
import pluginbase.sponge.messaging.SpongeMessagerProvider;
import pluginbase.sponge.minecraft.SpongeTools;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SpongePluginAgent<P> extends PluginAgent<P> {

    static {
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

    @NotNull
    @Override
    protected Settings loadSettings() {
        Settings defaults = getDefaultSettings();
        Settings settings = defaults;
        try {
            HoconSerializableConfigLoader loader = HoconSerializableConfigLoader.builder().setFile(new File(getDataFolder(),
                    "settings.conf")).build();
            CommentedConfigurationNode node = loader.load();
            Object value = node.getValue();
            if (value != null && value instanceof Settings) {
                getLog().info("Found settings!");
                settings = (Settings) value;
            } else {
                settings = defaults;
                node.setValue(settings);
                loader.save(node);
                getLog().info("Saved default settings!");
            }
            getLog().fine("Loaded settings file!");
        } catch (IOException e) {
            getLog().severe("There was a problem saving the config file!");
            e.printStackTrace();
        }
        getLog().setDebugLevel(getLog().getDebugLevel());
        return settings;
    }

    @NotNull
    @Override
    public DatabaseSettings loadDatabaseSettings(@NotNull DatabaseSettings defaults) {
        DatabaseSettings settings = defaults;
        try {
            HoconSerializableConfigLoader loader = HoconSerializableConfigLoader.builder().setFile(new File(getDataFolder(),
                    "settings.conf")).build();
            config = loader.load();
            Object value = config.getValue();
            if (value != null && value instanceof DatabaseSettings) {
                getLog().info("Found database settings!");
                settings = (DatabaseSettings) value;
            } else {
                settings = defaults;
                config.setValue(settings);
                loader.save(config);
                getLog().info("Saved default database settings!");
            }
            getLog().fine("Loaded db settings file!");
        } catch (IOException e) {
            getLog().severe("There was a problem saving the db config file!");
            e.printStackTrace();
        }
        return settings;
    }

    @Override
    protected void disablePlugin() {
        // TODO
    }

    @Override
    protected ServerInterface getServerInterface() {
        return serverInterface;
    }

    @Override
    protected void saveSettings() throws SendablePluginBaseException {
        if (config != null) {
            HoconSerializableConfigLoader loader = HoconSerializableConfigLoader.builder().setFile(new File(getDataFolder(),
                    "settings.conf")).build();
            config.setValue(getPluginBase().getSettings());
            try {
                loader.save(config);
            } catch (IOException e) {
                new PluginBaseException(e).logException(getLog(), Level.WARNING);
            }
            getLog().info("Saved config file!");
        } else {
            getLog().warning("Could not save config file as no config exists yet!");
        }
    }
}
