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
 * This command will typically perform a reload of the plugin's data though it's implementation
 * is ultimately up to the plugin.
 */
@CommandInfo(
        primaryAlias = "reload",
        desc = "Reloads the plugin/config.",
        max = 0
)
public class ReloadCommand extends BuiltInCommand {

    /** Permission for reload command. */
    public static final Perm PERMISSION = PermFactory.newPerm(PluginBase.class, "cmd.reload").usePluginName().commandPermission()
            .desc("Reloads the config file.").build();

    public final static Message RELOAD_HELP = Message.createMessage("cmd.reload.help", Theme.HELP + "Reloads the plugin, typically detecting any external changes in plugin files.");
    public final static Message RELOAD_COMPLETE = Message.createMessage("cmd.reload.complete", Theme.INFO + "===[ Reload Complete! ]===");

    protected ReloadCommand(@NotNull final PluginBase plugin) {
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
        return RELOAD_HELP;
    }

    /** {@inheritDoc} */
    @Override
    public boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        getPlugin().reloadConfig();
        getMessager().message(sender, RELOAD_COMPLETE);
        return true;
    }
}
