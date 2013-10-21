package pluginbase.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.messages.Message;

public interface Properties {

    Message CANNOT_SET_PROPERTY = Message.createMessage("properties.cannot_set_property",
            "$-The property '$v%s$-' may not be set in this way.");

    Message CANNOT_MODIFY_NON_COLLECTION = Message.createMessage("properties.cannot_modify_non_collection",
            "$-The property '$v%s$-' cannot be added/removed/cleared.");

    @NotNull
    String[] getAllPropertyNames();

    @Nullable
    Object getProperty(@NotNull String name) throws NoSuchFieldException;

    void setProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void addProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void removeProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    void clearProperty(@NotNull String name, @Nullable String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException;

    @Nullable
    Object getPropertyUnchecked(@NotNull String name);

    boolean setPropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean addPropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean removePropertyUnchecked(@NotNull String name, @NotNull String value);

    boolean clearPropertyUnchecked(@NotNull String name, @Nullable String value);
}
