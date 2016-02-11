package pluginbase.plugin.util;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class YamlLanguage {

    private YamlLanguage() {
        throw new AssertionError();
    }

    public static ConfigurationLoader getLoader(@NotNull File file) {
        return YAMLConfigurationLoader.builder().setFile(file).build();
    }
}
