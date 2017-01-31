package pluginbase.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginBaseAPI extends JavaPlugin {

    private static int API_VERSION = 1;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    public static int getApiVersion() {
        return API_VERSION;
    }

}
