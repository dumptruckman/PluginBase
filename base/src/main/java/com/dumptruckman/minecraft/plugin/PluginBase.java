package com.dumptruckman.minecraft.plugin;

import com.dumptruckman.minecraft.config.BaseConfig;

import java.io.File;

public interface PluginBase<C extends BaseConfig> {

    /**
     * @return the Config object which contains settings for this plugin.
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

    String getName();
    
    String getVersion();
    
    File getDataFolder();
    
    String getCommandPrefix();
}
