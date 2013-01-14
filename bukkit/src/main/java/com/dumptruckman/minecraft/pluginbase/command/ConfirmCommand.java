/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command;

import org.bukkit.command.CommandSender;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Enables debug-information.
 */
public abstract class ConfirmCommand {

    private static Set<String> staticKeys = new LinkedHashSet<String>();
    private static Set<String> staticPrefixedKeys = new LinkedHashSet<String>();

    public static void addStaticKey(String key) {
        staticKeys.add(key);
    }

    public static void addStaticPrefixedKey(String key) {
        staticPrefixedKeys.add(key);
    }

    public ConfirmCommand() {
        /*this.setName(getMessager().getMessage(CommandMessages.CONFIRM_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefix() + " confirm");
        this.setArgRange(0, 0);
        for (String key : staticKeys) {
            this.addKey(key);
        }
        for (String key : staticPrefixedKeys) {
            this.addPrefixedKey(key);
        }
        this.addPrefixedKey(" confirm");
        this.addCommandExample("/" + plugin.getCommandPrefix() + " confirm");
        //this.setPerm(Perm.COMMAND_CONFIRM);*/
    }

    //@Override
    public void runCommand(CommandSender sender, List<String> args) {
        //getPlugin().getCommandHandler().confirmQueuedCommand(sender);
    }
}
