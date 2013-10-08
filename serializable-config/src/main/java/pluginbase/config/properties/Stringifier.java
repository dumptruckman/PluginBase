package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.field.PropertyVetoException;

public interface Stringifier {

    @NotNull
    String toString(@NotNull Object value);

    @NotNull
    Object valueOf(@NotNull String value, @NotNull Class desiredType) throws PropertyVetoException;
}
