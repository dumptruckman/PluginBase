/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.CommandHandler;
import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Confirms queued commands.
 */
@CommandInfo(
        primaryAlias = "confirm",
        directlyPrefixPrimary = true,
        desc = "Confirms a previously entered command.",
        prefixedAliases = "confirm"
)
public class ConfirmCommand extends BaseBuiltInCommand {

    public final static Message PERMISSION = Message.createMessage("cmd.confirm.help", "Confirms the usage of a previously entered command, if required.");

    /** Permission for confirm command. */
    public static final Perm CONFIRM_COMMAND_PERM = PermFactory.newPerm(PluginBase.class, "cmd.confirm").usePluginName().commandPermission()
            .desc("If you have not been prompted to use this, it will not do anything.").build();

    private final static List<String> STATIC_KEYS = new ArrayList<String>();

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

    protected ConfirmCommand(@NotNull final PluginBase plugin) {
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
        return CONFIRM_COMMAND_PERM;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return PERMISSION;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        if (!getPlugin().getCommandHandler().confirmCommand(sender)) {
            getMessager().message(sender, CommandHandler.NO_QUEUED_COMMANDS);
        }
        return true;
    }
}
