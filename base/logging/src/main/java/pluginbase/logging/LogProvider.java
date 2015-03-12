package pluginbase.logging;

import org.jetbrains.annotations.NotNull;

/**
 * Used for plugins to indicate that they provide a {@link pluginbase.logging.PluginLogger}.
 */
public interface LogProvider {

    /**
     * Returns the PluginLogger used by this plugin.
     *
     * @return the PluginLogger used by this plugin.
     */
    @NotNull
    PluginLogger getLog();
}
