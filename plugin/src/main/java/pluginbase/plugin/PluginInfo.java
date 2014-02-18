package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;

/**
 * Contains information about the server plugin.
 */
public interface PluginInfo {

    /**
     * Gets the name of the plugin.
     *
     * @return the name of the plugin.
     */
    @NotNull
    String getName();

    /**
     * Gets the plugin version.
     *
     * @return the plugin version.
     */
    @NotNull
    String getVersion();
}
