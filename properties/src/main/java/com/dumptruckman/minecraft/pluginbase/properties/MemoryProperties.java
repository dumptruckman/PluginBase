package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryProperties extends AbstractProperties implements NestedProperties {

    protected final Map<String, Object> data;

    private final boolean autoDefaults;

    public MemoryProperties(final boolean autoDefaults, @NotNull final Class... configClasses) {
        super(configClasses);
        data = new HashMap<String, Object>(getProperties().size());
        this.autoDefaults = autoDefaults;
        if (autoDefaults) {
            setDefaults();
        }
    }

    @Override
    public void flush() { }

    @Override
    public void reload() { }

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

    @Override
    public <T> T get(SimpleProperty<T> entry) throws IllegalArgumentException {
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

    @Override
    public <T> List<T> get(ListProperty<T> entry) {
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
        return (List) obj;
    }

    @Override
    public <T> Map<String, T> get(MappedProperty<T> entry) {
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
        return (Map<String, T>) obj;
    }

    @Override
    public <T> T get(MappedProperty<T> entry, String key) {
        Map<String, T> map;
        try {
            map = get(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        return entry.getType().cast(obj);
    }

    @Override
    public NestedProperties get(NestedProperty entry) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        return getNestedProperties(entry);
    }

    @Override
    public <T> boolean set(SimpleProperty<T> entry, T value) {
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

    @Override
    public <T> boolean set(ListProperty<T> entry, List<T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getData().put(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    @Override
    public <T> boolean set(MappedProperty<T> entry, Map<String, T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getData().put(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    @Override
    public <T> boolean set(MappedProperty<T> entry, String key, T value) {
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
        ((Map<String, T>) obj).put(key, value);
        changed(entry);
        return true;
    }

    @Override
    public String toString() {
        return getData().toString();
    }
}
