/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandPerms;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This command will typically perform a reload of the plugin's data though it's implementation
 * is ultimately up to the plugin.
 */
@CommandInfo(
        primaryAlias = "reload",
        desc = "Reloads the plugin/config.",
        max = 0
)
public class ReloadCommand extends BaseBuiltInCommand {

    private static final List<String> STATIC_KEYS = new ArrayList<String>();

    /**
     * Adds an alias to this built in command.
     * <p/>
     * Allows adding aliases to a built in command which is not normally possible since you cannot
     * add CommandInfo annotations to them.
     *
     * @param key The alias to add.
     */
    public static void addStaticAlias(@NotNull final String key) {
        STATIC_KEYS.add(key);
    }

    protected ReloadCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public List<String> getStaticAliases() {
        return STATIC_KEYS;
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return CommandPerms.COMMAND_RELOAD;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return CommandMessages.RELOAD_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        getPlugin().reloadConfig();
        getMessager().message(sender, CommandMessages.RELOAD_COMPLETE);
        return true;
    }
}
