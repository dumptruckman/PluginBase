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
public abstract class PluginCommand<P extends BukkitPlugin> extends Command<P> {

    public PluginCommand(P plugin) {
        super(plugin);
    }

    /**
     * Retrieves the Messager associated with the BukkitPlugin this command is for.
     * @return
     */
    protected Messager getMessager() {
        return getPlugin().getMessager();
    }

    /**
     * Adds a command key (alias) that is prefixed with the Plugin's command prefix.
     *
     * @param key
     */
    public final void addPrefixedKey(String key) {
        for (String prefix : (List<String>) getPlugin().getCommandPrefixes()) {
            this.addKey(prefix + key);
        }
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);
}
