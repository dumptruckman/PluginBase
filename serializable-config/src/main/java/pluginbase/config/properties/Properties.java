package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyVetoException;

public interface Properties {

    @Nullable
    Object getProperty(@NotNull String... name) throws NoSuchFieldException, IllegalArgumentException;

    void setProperty(@Nullable Object value, @NotNull String... name) throws NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    @Nullable
    Object getPropertyUnchecked(@NotNull String... name) throws IllegalArgumentException;

    boolean setPropertyUnchecked(@Nullable Object value, @NotNull String... name) throws IllegalArgumentException;
}
