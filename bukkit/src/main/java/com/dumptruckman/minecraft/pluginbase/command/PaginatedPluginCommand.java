/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command;

public abstract class PaginatedPluginCommand<T> extends PaginatedCommand<T> {

    public PaginatedPluginCommand() {
        //super(plugin);
    }

    public final void addPrefixedKey(String key) {
    }

    //protected Messager getMessager() {
    //    return getPlugin().getMessager();
    //}

    //@Override
    //public abstract void runCommand(CommandSender sender, List<String> args);
}
