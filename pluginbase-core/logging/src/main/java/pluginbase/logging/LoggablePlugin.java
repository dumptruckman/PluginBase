/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.logging;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Any plugin wishing to use the PluginLogger must implement this method.
 * <br>
 * In a Bukkit plugin this is as simple as adding this class in an implements clause.
 */
public interface LoggablePlugin {

    /**
     * Returns the name of the plugin.
     *
     * @return The name of the plugin.
     */
    @NotNull
    String getName();

    /**
     * Returns the folder where a plugin's data is stored.
     *
     * @return Returns the folder where a plugin's data is stored.
     */
    @NotNull
    File getDataFolder();
}
