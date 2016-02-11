package pluginbase.plugin.util;

import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class GsonLanguage {

    private GsonLanguage() {
        throw new AssertionError();
    }

    public static ConfigurationLoader getLoader(@NotNull File file) {
        return GsonConfigurationLoader.builder().setFile(file).build();
    }
}
