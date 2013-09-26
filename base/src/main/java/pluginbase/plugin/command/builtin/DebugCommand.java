/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.BaseConfig;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "debug",
        desc = "Turns debug mode on/off",
        usage = "[1|2|3|off]",
        max = 1
)
public class DebugCommand extends BaseBuiltInCommand {

    /** Permission for debug command. */
    public static final Perm PERMISSION = PermFactory.newPerm(PluginBase.class, "cmd.debug").usePluginName().commandPermission()
            .desc("Spams the console a bunch.").build();

    public final static Message DEBUG_HELP = Message.createMessage("cmd.debug.help", Theme.HELP + "Enables or disable debug mode."
            + "  When enabled the console will be spammed with lots of information useful for helping developers debug.");
    public final static Message DEBUG_SET = Message.createMessage("cmd.debug.set", Theme.SUCCESS + "Debug mode is " + Theme.VALUE + "%s");
    public final static Message DEBUG_DISABLED = Message.createMessage("cmd.debug.off", Theme.SUCCESS + "Debug mode is " + Theme.VALUE + Theme.IMPORTANT + "OFF");
    public final static Message INVALID_DEBUG = Message.createMessage("debug.invalid",
            Theme.ERROR + "Invalid debug level.  Please use number 0-3.  " + Theme.INFO + "(3 being many many messages!)");

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
                getPlugin().getLog().setDebugLevel(getPlugin().config().get(BaseConfig.DEBUG_MODE));
                try {
                    getPlugin().config().flush();
                } catch (PluginBaseException e) {
                    if (!(e instanceof SendablePluginBaseException)) {
                        e = new SendablePluginBaseException(e);
                    }
                    ((SendablePluginBaseException) e).sendException(getMessager(), sender);
                }
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
            getPlugin().getLog().fine("%s debug ENABLED", p.getPluginInfo().getName());
        }
    }
}
