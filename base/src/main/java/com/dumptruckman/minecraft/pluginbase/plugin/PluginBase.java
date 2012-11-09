/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.logging.LoggablePlugin;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.SQLConfig;
import com.dumptruckman.minecraft.pluginbase.database.SQLDatabase;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandHandler;
import com.dumptruckman.minecraft.pluginbase.server.ServerInterface;
import org.mcstats.Metrics;

import java.io.File;
import java.util.List;

public interface PluginBase<C extends BaseConfig> extends LoggablePlugin {

    /**
     * @return the Properties object which contains settings for this plugin.
     */
    C config();

    /**
     * Gets the server's root-folder as {@link java.io.File}.
     *
     * @return The server's root-folder
     */
    File getServerFolder();

    /**
     * Sets this server's root-folder.
     *
     * @param newServerFolder The new server-root
     */
    void setServerFolder(File newServerFolder);

    PluginInfo getPluginInfo();
    
    File getDataFolder();
    
    String getCommandPrefix();

    SQLDatabase getDB();

    SQLConfig sqlConfig();

    Metrics getMetrics();

    void reloadConfig();

    Messager getMessager();

    List<String> dumpVersionInfo();

    ServerInterface getServerInterface();

    CommandHandler getCommandHandler();
}
