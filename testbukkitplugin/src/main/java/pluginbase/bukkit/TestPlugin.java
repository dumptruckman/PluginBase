package pluginbase.bukkit;

import pluginbase.bukkit.AbstractBukkitPlugin;
import pluginbase.bukkit.properties.YamlProperties;
import pluginbase.messages.PluginBaseException;
import pluginbase.plugin.BaseConfig;
import pluginbase.properties.Properties;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestPlugin extends AbstractBukkitPlugin {

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }

    @NotNull
    @Override
    protected Properties getNewConfig() throws PluginBaseException {
        return new YamlProperties.Loader(getLog(), new File(getDataFolder(), "config.yml"), BaseConfig.class).load();
    }
}
