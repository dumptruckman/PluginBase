package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldInstance;
import pluginbase.config.field.PropertyVetoException;

/**
 * A property handler defines how to a property's value is changeable through the methods in {@link Properties}.
 *
 * @param <T> The type of value this handler is responsible for.
 */
public interface PropertyHandler<T> {

    void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException;

    void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException;

    void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException;

    void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException;
}
