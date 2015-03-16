import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.plugin.PluginContainer;
import pluginbase.plugin.PluginInfo;

class SpongePluginInfo implements PluginInfo {

    @NotNull
    private final PluginContainer pluginContainer;

    SpongePluginInfo(@NotNull final PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    @NotNull
    @Override
    public String getName() {
        return pluginContainer.getName();
    }

    @NotNull
    @Override
    public String getVersion() {
        return pluginContainer.getVersion();
    }
}
