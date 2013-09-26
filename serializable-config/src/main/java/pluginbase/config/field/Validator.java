package pluginbase.config.field;

import org.jetbrains.annotations.Nullable;

public interface Validator<T> {

    @Nullable
    T validateChange(@Nullable T newValue, @Nullable T oldValue) throws PropertyVetoException;
}
