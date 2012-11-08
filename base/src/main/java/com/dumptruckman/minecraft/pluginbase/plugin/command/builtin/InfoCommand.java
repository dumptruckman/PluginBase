/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.sk89q.minecraft.util.commands.CommandContext;

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
public class InfoCommand extends BuiltInCommand {

    private static List<String> staticKeys = new ArrayList<String>();

    public static void addStaticAlias(String key) {
        staticKeys.add(key);
    }

    @Override
    public List<String> getStaticAliases() {
        return staticKeys;
    }

    @Override
    public Perm getPerm() {
        return Perm.COMMAND_INFO;
    }

    @Override
    public Message getHelp() {
        return CommandMessages.INFO_HELP;
    }

    @Override
    public boolean runCommand(PluginBase<BaseConfig> p, BasePlayer sender, CommandContext context) {

        return true;
    }
}
