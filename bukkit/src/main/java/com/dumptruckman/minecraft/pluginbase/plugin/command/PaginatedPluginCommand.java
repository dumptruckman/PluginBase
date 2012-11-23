/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;

public abstract class PaginatedPluginCommand<T, P extends AbstractBukkitPlugin> extends PaginatedCommand<T, P> {

    /**
     * The reference to the core.
     */
    protected P plugin;

    public PaginatedPluginCommand(P plugin) {
        //super(plugin);
        this.plugin = plugin;
    }

    public final void addPrefixedKey(String key) {
    }

    //protected Messager getMessager() {
    //    return getPlugin().getMessager();
    //}

    //@Override
    //public abstract void runCommand(CommandSender sender, List<String> args);
}
