package pluginbase.bukkit;

import pluginbase.plugin.PluginInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

class BukkitPluginInfo implements PluginInfo {

    private final Plugin plugin;

    BukkitPluginInfo(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
