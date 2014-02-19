package pluginbase.plugin.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;
import pluginbase.plugin.PluginBase;

public abstract class PluginCommand<P> extends Command<PluginBase<P>> {

    protected PluginCommand(@NotNull PluginBase<P> plugin) {
        super(plugin);
    }

    protected P getPlugin() {
        return getPluginBase().getPlugin();
    }
}
