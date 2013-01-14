/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command;

import org.jetbrains.annotations.NotNull;

public abstract class PluginCommand {

    public PluginCommand() {
        super();
    }

    /**
     * Adds a command key (alias) that is prefixed with the Plugin's command prefix.
     *
     * @param key
     */
    public final void addPrefixedKey(@NotNull final String key) {
    }

    //@Override
    //public abstract void runCommand(CommandSender sender, List<String> args);
}
