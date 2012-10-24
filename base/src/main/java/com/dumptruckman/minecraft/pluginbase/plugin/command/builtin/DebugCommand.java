/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.sk89q.minecraft.util.commands.CommandContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "debug",
        desc = "Turns debug mode on/off",
        usage = "&6[1|2|3|off]",
        max = 1
)
public class DebugCommand extends BuiltInCommand {

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
        return Perm.COMMAND_DEBUG;
    }

    @Override
    public Message getHelp() {
        return CommandMessages.DEBUG_HELP;
    }

    @Override
    public void runCommand(PluginBase<BaseConfig> p, BasePlayer sender, CommandContext context) {
        if (context.argsLength() == 1) {
            int debugLevel = -1;
            try {
                debugLevel = context.getInteger(0);
            } catch (NumberFormatException e) {
                if (context.getString(0).equalsIgnoreCase("off")) {
                    debugLevel = 0;
                }
            }
            if (debugLevel > 3 || debugLevel < 0) {
                p.getMessager().bad(sender, Messages.INVALID_DEBUG);
            } else {
                p.config().set(BaseConfig.DEBUG_MODE, debugLevel);
                Logging.setDebugLevel(p.config().get(BaseConfig.DEBUG_MODE));
                p.config().save();
            }
        }
        displayDebugMode(p, sender);
    }

    private void displayDebugMode(PluginBase<BaseConfig> p, BasePlayer sender) {
        if (p.config().get(BaseConfig.DEBUG_MODE) == 0) {
            p.getMessager().normal(sender, CommandMessages.DEBUG_DISABLED, sender);
        } else {
            p.getMessager().normal(sender, CommandMessages.DEBUG_SET, p.config().get(BaseConfig.DEBUG_MODE).toString());
            Logging.fine("%s debug ENABLED", p.getPluginInfo().getName());
        }
    }
}
