/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * This command is requires as it is the base command for the plugin.
 * TODO: Decide what the hell it does and then maybe use it.
 */
@CommandInfo(
        primaryAlias = "",
        directlyPrefixPrimary = true,
        desc = "Gives some information about this plugin"
)
public class InfoCommand extends BuiltInCommand {

    /** Permission for info command. */
    public static final Perm PERMISSION = PermFactory.newPerm(PluginBase.class, "cmd.info").usePluginName().commandPermission()
            .desc("Gives some basic information about this plugin.").build();

    public final static Message INFO_HELP = Message.createMessage("cmd.info.help", Theme.HELP + "Gives some basic information about this plugin.");

    protected InfoCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return PERMISSION;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return INFO_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        //TODO
        return true;
    }
}
