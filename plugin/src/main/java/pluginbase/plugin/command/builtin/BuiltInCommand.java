package pluginbase.plugin.command.builtin;

import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * A simple base class for built in commands.  Just added for easy of creating built in commands.
 */
abstract class BuiltInCommand extends Command<PluginBase> {

    protected BuiltInCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
