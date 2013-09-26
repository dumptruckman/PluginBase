package pluginbase.config.field;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.SendablePluginBaseException;

public class PropertyVetoException extends SendablePluginBaseException {

    public PropertyVetoException(@NotNull BundledMessage languageMessage) {
        super(languageMessage);
    }

    public PropertyVetoException(@NotNull BundledMessage languageMessage, @NotNull Throwable throwable) {
        super(languageMessage, throwable);
    }

    public PropertyVetoException(@NotNull BundledMessage languageMessage, @NotNull PluginBaseException cause) {
        super(languageMessage, cause);
    }

    public PropertyVetoException(@NotNull PluginBaseException e) {
        super(e);
    }
}
