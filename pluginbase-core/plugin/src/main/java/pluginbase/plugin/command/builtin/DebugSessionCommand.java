/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.debugsession.DebugSession;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Enables debug-information.
 */
@CommandInfo(
        primaryAlias = "debugsession",
        prefixedAliases = "dbs",
        directlyPrefixedAliases = "dbs",
        desc = "Turns debug mode on/off",
        flags = "bph",
        usage = "[]"
)
public class DebugSessionCommand extends BuiltInCommand {

    public final static Message DEBUG_SESSION_HELP = Message.createMessage("cmd.debugsession.help", Theme.HELP + "Begins or ends a debug session.",
            "When first using command the console will be spammed with lots of debugging information.",
            "When used a second time, this spam will stop and a link will be given containing a large amount of information from your server.",
            "Give this link to a developer so they can help you debug the situation.");

    public final static Message DEBUG_SESSION_STARTED = Message.createMessage("cmd.debugsession.started", Theme.SUCCESS + "Debug session started!",
            Theme.INFO + "Type the command again to stop the session and get a link to the debug output.");

    public final static Message DEBUG_SESSION_STOPPED = Message.createMessage("cmd.debugsession.stopped", Theme.SUCCESS + "Debug session stopped!",
            Theme.INFO + "Standby for a link to your debug session output.",
            Theme.IMPORTANT + "Give the link to a developer if you're in need of assistance!");
    public final static Message DEBUG_SESSION_DUMPED = Message.createMessage("cmd.debugsession.dumped", Theme.INFO.toString() + Theme.IMPORTANT + "Debug session dumped here: " + Theme.VALUE + Theme.IMPORTANT3 + "%s");


    private final Perm perm;

    protected DebugSessionCommand(@NotNull final PluginBase plugin) {
        super(plugin);
        perm = PermFactory.newPerm(plugin.getPluginClass(), "cmd.debugsession").usePluginName().commandPermission()
                .desc("Compiles a debug report for a user to provide to plugin support.").build();
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return perm;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return DEBUG_SESSION_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        if (getCommandProvider().getDebugSessionManager().hasDebugSession(sender)) {
            stopDebugSession(sender, context);
        } else {
            startDebugSession(sender);
        }
        return true;
    }

    private void startDebugSession(@NotNull BasePlayer sender) {
        getCommandProvider().getLog().info("%s is starting a debug session.", sender.getName());
        if (!getCommandProvider().getDebugSessionManager().hasActiveDebugSessions()) {
            getCommandProvider().getLog().setDebugLevel(3);
            getMessager().message(sender, DebugCommand.DEBUG_SET, 3);
        }
        DebugSession debugSession = getCommandProvider().getDebugSessionManager().startDebugSession(sender);
        getMessager().message(sender, DEBUG_SESSION_STARTED);
        for (Object versionInfo : getCommandProvider().dumpVersionInfo()) {
            getCommandProvider().getLog().info(versionInfo.toString());
        }
        if (debugSession.getReminderTask() != null) {
            debugSession.setTaskId(getCommandProvider().getServerInterface().runTaskTimer(debugSession.getReminderTask(), 1200, 1200));
        }
    }

    private void stopDebugSession(@NotNull BasePlayer sender, @NotNull CommandContext context) {
        DebugSession debugSession = getCommandProvider().getDebugSessionManager().stopDebugSession(sender);
        if (debugSession.getReminderTask() != null && debugSession.getTaskId() != -1) {
            getCommandProvider().getServerInterface().cancelTask(debugSession.getTaskId());
            debugSession.setTaskId(-1);
        }
        getMessager().message(sender, DEBUG_SESSION_STOPPED);
        Set<Character> flags = new LinkedHashSet<Character>(context.getFlags());
        if (flags.isEmpty()) {
            flags.add('h');
        }
        getCommandProvider().getServerInterface().runTaskAsynchronously(new PasteServiceTask(getCommandProvider(), flags, debugSession.getCompiledOutput(), sender, DEBUG_SESSION_DUMPED));
        getCommandProvider().getLog().info("%s has stopped their debug session.", sender.getName());
        if (!getCommandProvider().getDebugSessionManager().hasActiveDebugSessions()) {
            getCommandProvider().getLog().setDebugLevel(getCommandProvider().getSettings().getDebugLevel());
            getMessager().message(sender, DebugCommand.DEBUG_SET, getCommandProvider().getSettings().getDebugLevel());
        }
    }
}
