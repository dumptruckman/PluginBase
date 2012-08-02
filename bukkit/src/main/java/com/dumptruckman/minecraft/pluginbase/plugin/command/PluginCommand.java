/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.commandhandler.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A generic Multiverse-command.
 */
public abstract class PluginCommand<P extends BukkitPlugin> extends Command {

    /**
     * The reference to the core.
     */
    protected P plugin;

    public PluginCommand(P plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    protected Messager getMessager() {
        return plugin.getMessager();
    }

    public final void addPrefixedKey(String key) {
        for (String prefix : (List<String>) plugin.getCommandPrefixes()) {
            this.addKey(prefix + key);
        }
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);
}
