package pluginbase.plugin.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;
import pluginbase.command.QueuedCommand;
import pluginbase.plugin.PluginBase;

public abstract class QueuedPluginCommand<P> extends QueuedCommand<PluginBase<P>> {

    protected QueuedPluginCommand(@NotNull PluginBase<P> plugin) {
        super(plugin);
    }

    protected P getPlugin() {
        return getPluginBase().getPlugin();
    }
}
