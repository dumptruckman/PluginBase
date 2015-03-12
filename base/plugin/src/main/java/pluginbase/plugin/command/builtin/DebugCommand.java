/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "debug",
        desc = "Turns debug mode on/off",
        usage = "[1|2|3|off]",
        max = 1
)
public class DebugCommand extends BuiltInCommand {

    public final static Message DEBUG_HELP = Message.createMessage("cmd.debug.help", Theme.HELP + "Enables or disable debug mode."
            + "  When enabled the console will be spammed with lots of information useful for helping developers debug.");
    public final static Message DEBUG_SET = Message.createMessage("cmd.debug.set", Theme.SUCCESS + "Debug mode is " + Theme.VALUE + "%s");
    public final static Message DEBUG_DISABLED = Message.createMessage("cmd.debug.off", Theme.SUCCESS + "Debug mode is " + Theme.VALUE + Theme.IMPORTANT + "OFF");
    public final static Message INVALID_DEBUG = Message.createMessage("debug.invalid",
            Theme.ERROR + "Invalid debug level.  Please use number 0-3.  " + Theme.INFO + "(3 being many many messages!)");

    private final Perm perm;

    protected DebugCommand(@NotNull final PluginBase plugin) {
        super(plugin);
        perm = PermFactory.newPerm(plugin.getPluginClass(), "cmd.debug").usePluginName().commandPermission()
                .desc("Spams the console a bunch.").build();
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return perm;
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
                try {
                    getCommandProvider().getSettings().setDebugLevel(debugLevel);
                    getCommandProvider().getLog().setDebugLevel(getCommandProvider().getSettings().getDebugLevel());
                    getCommandProvider().saveSettings();
                } catch (SendablePluginBaseException e) {
                    e.sendException(getMessager(), sender);
                }
            }
        }
        displayDebugMode(getCommandProvider(), sender);
        return true;
    }

    private void displayDebugMode(PluginBase p, BasePlayer sender) {
        if (p.getSettings().getDebugLevel() == 0) {
            p.getMessager().message(sender, DEBUG_DISABLED, sender);
        } else {
            p.getMessager().message(sender, DEBUG_SET, p.getSettings().getDebugLevel());
            getCommandProvider().getLog().fine("%s debug ENABLED", p.getPluginInfo().getName());
        }
    }
}
