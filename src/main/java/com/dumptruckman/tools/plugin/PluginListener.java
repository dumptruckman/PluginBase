package com.dumptruckman.tools.plugin;

import org.bukkit.event.Listener;

/**
 * Class that handles all bukkit events for this Plugin.
 */
public class PluginListener implements Listener {

    private PluginBase plugin;

    public PluginListener(PluginBase plugin) {
        this.plugin = plugin;
    }
}
