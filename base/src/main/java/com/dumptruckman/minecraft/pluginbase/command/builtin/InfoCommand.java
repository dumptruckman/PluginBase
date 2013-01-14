/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "",
        directlyPrefixPrimary = true,
        desc = "Gives some information about this plugin"
)
public class InfoCommand extends BaseBuiltInCommand {

    private static List<String> staticKeys = new ArrayList<String>();

    public static void addStaticAlias(String key) {
        staticKeys.add(key);
    }

    protected InfoCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    @Override
    public List<String> getStaticAliases() {
        return staticKeys;
    }

    @Override
    public Perm getPerm() {
        return CommandPerms.COMMAND_INFO;
    }

    @Override
    public Message getHelp() {
        return CommandMessages.INFO_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        //TODO
        return true;
    }
}
