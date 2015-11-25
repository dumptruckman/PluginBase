package pluginbase.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcstats.Metrics;
import pluginbase.bukkit.command.BukkitCommandProvider;
import pluginbase.bukkit.messaging.BukkitMessagerProvider;
import pluginbase.bukkit.minecraft.BukkitTools;
import pluginbase.bukkit.permission.BukkitPermFactory;
import pluginbase.messages.messaging.MessagerFactory;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginAgent;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;

import java.io.File;
import java.io.IOException;

public class BukkitPluginAgent<P> extends PluginAgent<P> {

    static {
        PermFactory.registerPermissionFactory(BukkitPermFactory.class);
        MessagerFactory.registerMessagerProvider(new BukkitMessagerProvider());
    }

    @NotNull
    public static <P> BukkitPluginAgent<P> getPluginAgent(@NotNull Class<P> pluginInterface, @NotNull Plugin plugin, @NotNull String commandPrefix) {
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new BukkitPluginAgent<P>(pluginInterface, plugin, commandPrefix, true);
    }

    @NotNull
    public static <P> BukkitPluginAgent<P> getPluginAgentNoQueuedCommands(@NotNull Class<P> pluginInterface, @NotNull Plugin plugin, @NotNull String commandPrefix) {
        if (!pluginInterface.isInstance(plugin)) {
            throw new IllegalArgumentException("pluginInterface must be a superclass or superinterface of plugin.");
        }
        return new BukkitPluginAgent<P>(pluginInterface, plugin, commandPrefix, false);
    }

    @NotNull
    private final Plugin plugin;
    private PluginInfo pluginInfo;
    private final ServerInterface serverInterface;
    @Nullable
    private Metrics metrics = null;

    private BukkitPluginAgent(@NotNull Class<P> pluginInterface, @NotNull Plugin plugin, @NotNull String commandPrefix, boolean queuedCommands) {
        super(pluginInterface, (P) plugin, queuedCommands ? BukkitCommandProvider.getBukkitCommandProvider(plugin, commandPrefix) : BukkitCommandProvider.getBukkitCommandProviderNoQueuedCommands(plugin, commandPrefix));
        this.plugin = plugin;
        this.serverInterface = new BukkitServerInterface(plugin);
    }

    public void enableMetrics() throws IOException {
        getMetrics().enable();
    }

    public void disableMetrics() throws IOException {
        getMetrics().disable();
    }

    /**
     * Gets the metrics object for the plugin.
     *
     * @return the metrics object for the plugin or null if something went wrong while enabling one or if the
     * server chooses not to use metrics.
     */
    @NotNull
    public Metrics getMetrics() throws IOException {
        if (metrics == null) {
            metrics = new Metrics(plugin);
        }
        return metrics;
    }

    @NotNull
    @Override
    protected PluginInfo getPluginInfo() {
        if (pluginInfo == null) {
            pluginInfo = new BukkitPluginInfo(plugin);
        }
        return pluginInfo;
    }

    @NotNull
    @Override
    protected File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    protected void disablePlugin() {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    @Override
    protected ServerInterface getServerInterface() {
        return serverInterface;
    }

    public boolean callCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        if (!plugin.isEnabled()) {
            sender.sendMessage("This plugin is Disabled!");
            return true;
        }
        final BasePlayer wrappedSender = BukkitTools.wrapSender(sender);
        return callCommand(wrappedSender, command.getName(), args);
    }
}
