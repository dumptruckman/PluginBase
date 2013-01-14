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
        primaryAlias = "reload",
        desc = "Reloads the plugin/config.",
        max = 0
)
public class ReloadCommand extends BaseBuiltInCommand {

    private static final List<String> staticKeys = new ArrayList<String>();

    public static void addStaticAlias(String key) {
        staticKeys.add(key);
    }

    protected ReloadCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    @Override
    public List<String> getStaticAliases() {
        return staticKeys;
    }

    @Override
    public Perm getPerm() {
        return CommandPerms.COMMAND_RELOAD;
    }

    @Override
    public Message getHelp() {
        return CommandMessages.RELOAD_HELP;
    }

    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        getPlugin().reloadConfig();
        getMessager().message(sender, CommandMessages.RELOAD_COMPLETE);
        return true;
    }
}
