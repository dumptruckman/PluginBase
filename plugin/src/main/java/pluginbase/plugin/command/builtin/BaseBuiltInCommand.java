package pluginbase.plugin.command.builtin;

import pluginbase.command.CommandContext;
import pluginbase.command.builtin.BuiltInCommand;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple base class for built in commands.  Just added for easy of creating built in commands.
 */
abstract class BaseBuiltInCommand extends BuiltInCommand<PluginBase> {

    protected BaseBuiltInCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    /**
     * Gets any aliases added to a command via a static method for each built in command.
     * <p/>
     * These allow for custom aliases for the built in commands which can not be added through normal means.
     *
     * @return a list of aliases for the command.
     */
    @NotNull
    public abstract List<String> getStaticAliases();

    /** {@inheritDoc} */
    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
