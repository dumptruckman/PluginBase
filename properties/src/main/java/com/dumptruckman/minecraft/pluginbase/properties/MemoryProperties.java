package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple, memory only, backed properties implementation.
 */
public class MemoryProperties extends AbstractProperties implements NestedProperties {

    private final Map<String, Object> data;
    private final boolean autoDefaults;

    /**
     * Constructs a new MemoryProperties object configured to use the indicated classes as repositories for {@link Property} objects.
     *
     * @param autoDefaults true will cause default values to be assigned immediately for all properties.
     * @param configClasses the classes that contain the {@link Property} objects that "belong" to this Properties object.
     */
    protected MemoryProperties(final boolean autoDefaults, @NotNull final Class... configClasses) {
        super(configClasses);
        data = new HashMap<String, Object>(getProperties().size());
        this.autoDefaults = autoDefaults;
        if (autoDefaults) {
            setDefaults();
        }
    }

    /**
     * Creates a new MemoryProperties object configured to use the indicated classes as repositories for {@link Property} objects.
     *
     * @param configClasses the classes that contain the {@link Property} objects that "belong" to this Properties object.
     */
    public static Properties newMemoryProperties(@NotNull final Class... configClasses) {
        return newMemoryProperties(true, configClasses);
    }

    /**
     * Creates a new MemoryProperties object configured to use the indicated classes as repositories for {@link Property} objects.
     *
     * @param autoDefaults true will cause default values to be assigned immediately for all properties.
     * @param configClasses the classes that contain the {@link Property} objects that "belong" to this Properties object.
     */
    public static Properties newMemoryProperties(final boolean autoDefaults, @NotNull final Class... configClasses) {
        return new MemoryProperties(autoDefaults, configClasses);
    }

    /** {@inheritDoc} */
    @Override
    public void flush() { }

    /** {@inheritDoc} */
    @Override
    public void reload() { }

    /**
     * Gets the data map for this properties object.
     *
     * @return the data map for this properties object.
     */
    protected Map<String, Object> getData() {
        return this.data;
    }

    /**
     * Loads default settings for any missing config values.
     */
    protected void setDefaults() {
        for (Property path : getProperties()) {
            if (path instanceof ValueProperty) {
                ValueProperty valueProperty = (ValueProperty) path;
                if (getData().get(valueProperty.getName()) == null) {
                    if (valueProperty.isDeprecated()) {
                        continue;
                    }
                    if (valueProperty instanceof MappedProperty) {
                        Logging.fine("Config: Defaulting map for '%s'", valueProperty.getName());
                        if (valueProperty.getDefault() != null) {
                            getData().put(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            getData().put(valueProperty.getName(), ((MappedProperty) valueProperty).getNewTypeMap());
                        }
                    } else if (valueProperty instanceof ListProperty) {
                        ListProperty listPath = (ListProperty) valueProperty;
                        Logging.fine("Config: Defaulting list for '%s'", valueProperty.getName());
                        if (listPath.getDefault() != null) {
                            getData().put(valueProperty.getName(), listPath.getDefault());
                        } else {
                            getData().put(valueProperty.getName(), listPath.getNewTypeList());
                        }
                    } else if (valueProperty.getDefault() != null) {
                        Logging.fine("Config: Defaulting '%s' to %s", valueProperty.getName(), valueProperty.getDefault());
                        getData().put(valueProperty.getName(), valueProperty.getDefault());
                    }
                }
            } else if (path instanceof NestedProperty) {
                getNestedProperties((NestedProperty) path);
            }
        }
    }

    private NestedProperties getNestedProperties(@NotNull final NestedProperty nestedProperty) {
        Object obj = getData().get(nestedProperty.getName());
        if (obj == null || !(obj instanceof NestedProperties)) {
            obj = new MemoryProperties(autoDefaults, nestedProperty.getType());
            getData().put(nestedProperty.getName(), obj);
        }
        return (NestedProperties) obj;
    }

    private Object getEntryValue(ValueProperty valueProperty) throws IllegalArgumentException {
        if (!isInConfig(valueProperty)) {
            throw new IllegalArgumentException("property not registered to this config!");
        }
        Object obj = getData().get(valueProperty.getName());
        if (obj == null) {
            if (valueProperty.shouldDefaultIfMissing()) {
                obj = valueProperty.getDefault();
                getData().put(valueProperty.getName(), obj);
            }
        }
        return obj;
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public <T> T get(@NotNull final SimpleProperty<T> entry) throws IllegalArgumentException {
        Object obj;
        try {
            obj = getEntryValue(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (obj == null) {
            return null;
        }
        return entry.getType().cast(obj);
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public <T> List<T> get(@NotNull final ListProperty<T> entry) {
        Object obj;
        try {
            obj = getEntryValue(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List)) {
            obj = new ArrayList<Object>();
        }
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        final List<T> list = (List) obj;
        return list;
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public <T> Map<String, T> get(@NotNull final MappedProperty<T> entry) {
        Object obj;
        try {
            obj = getEntryValue(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Map)) {
            obj = new HashMap<String, T>();
        }
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        final Map<String, T> map = (Map<String, T>) obj;
        return map;
    }

    /** {@inheritDoc} */
    @Override
    @Nullable
    public <T> T get(@NotNull final MappedProperty<T> entry, @NotNull final String key) {
        Map<String, T> map;
        try {
            map = get(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (map == null) {
            return null;
        }
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        return entry.getType().cast(obj);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NestedProperties get(@NotNull final NestedProperty entry) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        return getNestedProperties(entry);
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final SimpleProperty<T> entry, @Nullable final T value) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        if (value == null) {
            getData().remove(entry.getName());
            changed(entry);
            return true;
        }
        if (!isValid(entry, value)) {
            return false;
        }
        getData().put(entry.getName(), value);
        changed(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final ListProperty<T> entry, @Nullable final List<T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getData().put(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final MappedProperty<T> entry, @Nullable final Map<String, T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getData().put(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final MappedProperty<T> entry, @NotNull final String key, @Nullable final T value) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        if (!isValid(entry, value)) {
            return false;
        }
        Object obj = getData().get(entry.getName());
        if (obj == null || !(obj instanceof Map)) {
            obj = new HashMap<String, T>();
            getData().put(entry.getName(), obj);
        }
        @SuppressWarnings("unchecked")
        Map<String, T> map = (Map<String, T>) obj;
        map.put(key, value);
        changed(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getData().toString();
    }
}
