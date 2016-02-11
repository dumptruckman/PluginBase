package pluginbase.plugin.util;

import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JsonLanguage {

    private JsonLanguage() {
        throw new AssertionError();
    }

    public static ConfigurationLoader getLoader(@NotNull File file) {
        return JSONConfigurationLoader.builder().setFile(file).build();
    }
}
