/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.CommandInfo;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;
import com.dumptruckman.minecraft.pluginbase.plugin.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

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
public class DebugCommand extends BaseBuiltInCommand {

    /** Permission for debug command. */
    public static final Perm PERMISSION = PermFactory.newPerm(PluginBase.class, "cmd.debug").usePluginName().commandPermission()
            .desc("Spams the console a bunch.").build();

    public final static Message DEBUG_HELP = new Message("cmd.debug.help", "Enables or disable debug mode."
            + "When enabled the console will be spammed with lots of information useful for helping developers debug.");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is &2%s");
    public final static Message DEBUG_DISABLED = new Message("cmd.debug.off", "Debug mode is &cOFF");
    public final static Message INVALID_DEBUG = new Message("debug.invalid",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");

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

    protected DebugCommand(@NotNull final PluginBase plugin) {
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
        return PERMISSION;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return DEBUG_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
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
                getMessager().message(sender, INVALID_DEBUG);
            } else {
                getPlugin().config().set(BaseConfig.DEBUG_MODE, debugLevel);
                Logging.setDebugLevel(getPlugin().config().get(BaseConfig.DEBUG_MODE));
                getPlugin().config().flush();
            }
        }
        displayDebugMode(getPlugin(), sender);
        return true;
    }

    private void displayDebugMode(PluginBase p, BasePlayer sender) {
        if (p.config().get(BaseConfig.DEBUG_MODE) == 0) {
            p.getMessager().message(sender, DEBUG_DISABLED, sender);
        } else {
            p.getMessager().message(sender, DEBUG_SET, p.config().get(BaseConfig.DEBUG_MODE).toString());
            Logging.fine("%s debug ENABLED", p.getPluginInfo().getName());
        }
    }
}
