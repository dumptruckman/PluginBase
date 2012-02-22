package com.dumptruckman.tools.plugin;

import com.dumptruckman.tools.config.ConfigBase;
import com.dumptruckman.tools.locale.Messaging;
import com.pneumaticraft.commandhandler.CommandHandler;
import org.bukkit.plugin.Plugin;

import java.io.File;

public interface PluginBase<C extends ConfigBase> extends Plugin, Messaging {

    /**
     * @return The instance of CommandHandler used by this plugin.
     */
    CommandHandler getCommandHandler();

    /**
     * @return the Config object which contains settings for this plugin.
     */
    C getSettings();

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

    String getCommandPrefix();
    
    String getPluginName();
}
