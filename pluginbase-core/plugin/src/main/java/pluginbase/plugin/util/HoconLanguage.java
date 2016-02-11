package pluginbase.plugin.util;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class HoconLanguage {

    private HoconLanguage() {
        throw new AssertionError();
    }

    public static ConfigurationLoader getLoader(@NotNull File file) {
        return HoconConfigurationLoader.builder().setFile(file).build();
    }
}
