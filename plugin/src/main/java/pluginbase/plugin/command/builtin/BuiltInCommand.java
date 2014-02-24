package pluginbase.plugin.command.builtin;

import pluginbase.command.Command;
import pluginbase.command.CommandContext;
import pluginbase.command.CommandProvider;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

/**
 * A simple base class for built in commands for the Plugin module. Your own commands should probably NOT extend this.
 */
public abstract class BuiltInCommand extends Command {

    protected BuiltInCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    protected PluginBase getCommandProvider() {
        return (PluginBase) super.getCommandProvider();
    }

    /** {@inheritDoc} */
    @Override
    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
