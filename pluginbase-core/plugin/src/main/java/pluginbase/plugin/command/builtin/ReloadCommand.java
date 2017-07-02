/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.CommandInfo;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.Theme;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * This command will typically perform a reload of the plugin's data though it's implementation
 * is ultimately up to the plugin.
 */
@CommandInfo(
        primaryAlias = "reload",
        desc = "Reloads the plugin/config.",
        max = 0
)
public class ReloadCommand extends BuiltInCommand {

    public final static Message RELOAD_HELP = Messages.createMessage("cmd.reload.help", Theme.HELP + "Reloads the plugin, typically detecting any external changes in plugin files.");
    public final static Message RELOAD_COMPLETE = Messages.createMessage("cmd.reload.complete", Theme.INFO + "===[ Reload Complete! ]===");

    private final Perm perm;

    protected ReloadCommand(@NotNull final PluginBase plugin) {
        super(plugin);
        perm = PermFactory.newPerm(plugin.getPluginClass(), "cmd.reload").usePluginName().commandPermission()
                .desc("Reloads the config file.").build();
    }

    /** {@inheritDoc} */
    @Override
    public Perm getPerm() {
        return perm;
    }

    /** {@inheritDoc} */
    @Override
    public Message getHelp() {
        return RELOAD_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        getCommandProvider().reloadConfig();
        getMessager().message(sender, RELOAD_COMPLETE);
        return true;
    }
}
