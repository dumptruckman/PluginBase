package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An interface that will allow for modifying the output of the version command.
 * <br>
 * See {@link pluginbase.plugin.PluginAgent#setVersionInfoModifier(VersionInfoModifier)}.
 */
public interface VersionInfoModifier {

    /**
     * This method will modify the information provided by the version command.
     *
     * @param versionInfo the list of strings that will be output by the version command, to be modified.
     * @return the modified list of strings that will be output by the version command.
     */
    @NotNull
    List<String> modifyVersionInfo(@NotNull List<String> versionInfo);
}
