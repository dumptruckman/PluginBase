package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;

public enum ConfigType {

    HOCON(".conf"),
    YAML(".yml"),
    JSON(".json"),
    GSON(".json")
    ;

    @NotNull
    private final String ext;

    ConfigType(@NotNull String ext) {
        this.ext = ext;
    }

    @NotNull
    public String getFileExtension() {
        return ext;
    }
}
