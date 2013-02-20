/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.bukkit.properties;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.properties.AbstractProperties;
import com.dumptruckman.minecraft.pluginbase.properties.ListProperty;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.Null;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.Property;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import com.dumptruckman.minecraft.pluginbase.properties.ValueProperty;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.DefaultSerializer;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.DefaultStringSerializer;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.StringStringSerializer;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlProperties extends AbstractProperties implements Properties {

    protected final CommentedYamlConfiguration config;

    private final Map<NestedProperty, NestedYamlProperties> nestMap = new HashMap<NestedProperty, NestedYamlProperties>();

    public AbstractYamlProperties(final CommentedYamlConfiguration config,
                                  final Class... configClasses) {
        super(configClasses);
        this.config = config;

        for (final Property property : entries.properties) {
            if (property instanceof ValueProperty) {
                final ValueProperty valueProperty = (ValueProperty) property;
                final Class type = valueProperty.getType();
                if (valueProperty.getDefaultSerializer() != null) {
                    setPropertySerializer(type, valueProperty.getDefaultSerializer());
                } else  if (!hasPropertySerializer(type)) {
                    if (type.equals(String.class)) {
                        setPropertySerializer(type, new StringStringSerializer(type));
                    } else {
                        try {
                            setPropertySerializer(type, new DefaultStringSerializer(type));
                        } catch (IllegalArgumentException e) {
                            setPropertySerializer(type, new DefaultSerializer(type));
                        }
                    }
                }
            }
        }
    }

    protected ConfigurationSection getConfig() {
        return this.config.getConfig();
    }

    protected ConfigurationOptions getConfigOptions() {
        return this.config.getConfig().options();
    }

    protected String getName() {
        return "";
    }

    protected void doComments(final CommentedYamlConfiguration config) {
        for (Property property : entries.properties) {
            final String path;
            if (!getName().isEmpty()) {
                path = getName() + getConfigOptions().pathSeparator() + property.getName();
            } else {
                path = property.getName();
            }
            config.addComment(path, property.getComments());
            if (property instanceof NestedProperty) {
                NestedYamlProperties nestedProperties = this.nestMap.get(property);
                if (nestedProperties != null) {
                    nestedProperties.doComments(config);
                }
            }
        }
    }

    protected void deserializeAll() {
        for (final Property property : entries.properties) {
            if (property instanceof ValueProperty) {
                final ValueProperty valueProperty = (ValueProperty) property;
                if (getConfig().get(valueProperty.getName()) != null) {
                    if (valueProperty instanceof MappedProperty) {
                        ConfigurationSection section = getConfig().getConfigurationSection(valueProperty.getName());
                        if (section == null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            for (String key : section.getKeys(false)) {
                                final Object obj = section.get(key);
                                if (obj != null) {
                                    final Object res = getPropertySerializer(valueProperty.getType()).deserialize(obj);
                                    if (isValid(valueProperty, res)) {
                                        section.set(key, res);
                                    } else {
                                        Logging.warning("Invalid value '" + obj + "' at '" + valueProperty.getName() + getConfigOptions().pathSeparator() + key + "'.  Value will be deleted!");
                                        section.set(key, null);
                                    }
                                }
                            }
                        }
                    } else if (valueProperty instanceof ListProperty) {
                        List list = getConfig().getList(valueProperty.getName());
                        if (list == null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            List newList = ((ListProperty) valueProperty).getNewTypeList();
                            for (int i = 0; i < list.size(); i++) {
                                final Object res = getPropertySerializer(valueProperty.getType()).deserialize(list.get(i));
                                if (isValid(valueProperty, res)) {
                                    newList.add(res);
                                } else {
                                    Logging.warning("Invalid value '" + res + "' at '" + valueProperty.getName() + "[" + i + "]'.  Value will be deleted!");
                                }
                            }
                            getConfig().set(valueProperty.getName(), newList);
                        }
                    } else if (valueProperty instanceof SimpleProperty && !valueProperty.getType().isAssignableFrom(Null.class)) {
                        Object obj = getConfig().get(valueProperty.getName());
                        if (obj == null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            Object res = getPropertySerializer(valueProperty.getType()).deserialize(obj);
                            if (isValid(valueProperty, res)) {
                                getConfig().set(valueProperty.getName(), res);
                            } else {
                                Logging.warning("Invalid value '" + obj + "' at '" + valueProperty.getName() + "'.  Value will be defaulted!");
                                getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                            }
                        }
                    }
                }
            } else if (property instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) property;
                final NestedYamlProperties nestedProperties = new NestedYamlProperties(config, this,
                        nestedProperty.getName(), nestedProperty.getType());
                nestedProperties.deserializeAll();
                getConfig().set(nestedProperty.getName(), nestedProperties.getConfig());
                this.nestMap.put(nestedProperty, nestedProperties);
            }
        }
    }

    protected void serializeAll(ConfigurationSection newConfig) {
        for (final Property property : entries.properties) {
            if (property instanceof ValueProperty) {
                final ValueProperty valueProperty = (ValueProperty) property;
                if (getConfig().get(valueProperty.getName()) != null) {
                    if (valueProperty instanceof MappedProperty) {
                        Object o = getConfig().get(valueProperty.getName());
                        if (o == null) {
                            Logging.fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        Map map;
                        if (o instanceof ConfigurationSection) {
                            map = ((ConfigurationSection) o).getValues(false);
                        } else if (!(o instanceof Map)) {
                            Logging.fine("Missing property: %s", valueProperty.getName());
                            continue;
                        } else {
                            map = (Map) o;
                        }
                        for (Object key : map.keySet()) {
                            Object obj = map.get(key);
                            if (valueProperty.getType().isInstance(obj)) {
                                if (obj != null) {
                                    map.put(key, getPropertySerializer(valueProperty.getType()).serialize(valueProperty.getType().cast(obj)));
                                }
                            } else {
                                Logging.warning("Could not serialize: %s", valueProperty.getName());
                            }
                        }
                        newConfig.set(valueProperty.getName(), map);
                    } else if (valueProperty instanceof ListProperty) {
                        List list = getConfig().getList(valueProperty.getName());
                        if (list == null) {
                            Logging.fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        List newList = new ArrayList(list.size());
                        for (Object obj : list) {
                            if (valueProperty.getType().isInstance(obj)) {
                                if (obj != null) {
                                    newList.add(getPropertySerializer(valueProperty.getType()).serialize(valueProperty.getType().cast(obj)));
                                }
                            } else {
                                Logging.warning("Could not serialize: %s", valueProperty.getName());
                            }
                        }
                        newConfig.set(valueProperty.getName(), newList);
                    } else if (valueProperty instanceof SimpleProperty && !valueProperty.getType().isAssignableFrom(Null.class)) {
                        Object obj = getConfig().get(valueProperty.getName());
                        if (obj == null) {
                            Logging.fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        if (valueProperty.getType().isInstance(obj)) {
                            Object res = getPropertySerializer(valueProperty.getType()).serialize(valueProperty.getType().cast(obj));
                            newConfig.set(valueProperty.getName(), res);
                        } else {
                            Logging.warning("Could not serialize '%s' since value is '%s' instead of '%s'", valueProperty.getName(), obj.getClass(), valueProperty.getType());
                        }
                    }
                }
            } else if (property instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) property;
                final NestedYamlProperties nestedProperties = this.nestMap.get(nestedProperty);
                if (nestedProperties != null) {
                    nestedProperties.serializeAll(newConfig.createSection(nestedProperty.getName()));
                } else {
                    //TODO Warn
                }
            }
        }
    }

    /**
     * Loads default settings for any missing config values.
     */
    protected void setDefaults() {
        for (Property path : entries.properties) {
            if (path instanceof ValueProperty) {
                ValueProperty valueProperty = (ValueProperty) path;
                if (getConfig().get(valueProperty.getName()) == null) {
                    if (valueProperty.isDeprecated()) {
                        continue;
                    }
                    if (valueProperty instanceof MappedProperty) {
                        Logging.fine("Config: Defaulting map for '%s'", valueProperty.getName());
                        if (valueProperty.getDefault() != null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            getConfig().set(valueProperty.getName(), ((MappedProperty) valueProperty).getNewTypeMap());
                        }
                    } else if (valueProperty instanceof ListProperty) {
                        ListProperty listPath = (ListProperty) valueProperty;
                        Logging.fine("Config: Defaulting list for '%s'", valueProperty.getName());
                        if (listPath.getDefault() != null) {
                            getConfig().set(valueProperty.getName(), listPath.getDefault());
                        } else {
                            getConfig().set(valueProperty.getName(), listPath.getNewTypeList());
                        }
                    } else if (valueProperty.getDefault() != null) {
                        Logging.fine("Config: Defaulting '%s' to %s", valueProperty.getName(), valueProperty.getDefault());
                        getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                    }
                }
            } else if (path instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) path;
                final NestedYamlProperties nestedProperties = this.nestMap.get(nestedProperty);
                if (nestedProperties != null) {
                    nestedProperties.setDefaults();
                } else {
                    //TODO Warn
                }
            }
        }
    }

    private Object getEntryValue(ValueProperty valueProperty) throws IllegalArgumentException {
        if (!isInConfig(valueProperty)) {
            throw new IllegalArgumentException("property not registered to this config!");
        }
        Object obj = getConfig().get(valueProperty.getName());
        if (obj == null) {
            if (valueProperty.shouldDefaultIfMissing()) {
                obj = valueProperty.getDefault();
            }
        }
        return obj;
    }

    @Override
    public <T> T get(SimpleProperty<T> entry) throws IllegalArgumentException {
        Object obj = null;
        try {
            obj = getEntryValue(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (obj == null) {
            return null;
        }
        if (!entry.getType().isInstance(obj)) {
            Logging.fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, entry.getName());
            final Object res = getPropertySerializer(entry.getType()).deserialize(obj);
            if (isValid(entry, (T) res)) {
                obj = res;
            }
        }
        return entry.getType().cast(obj);
    }

    @Override
    public <T> List<T> get(ListProperty<T> entry) {
        Object obj = null;
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
        List list = (List) obj;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (!entry.getType().isInstance(o)) {
                Logging.fine("An invalid value of '%s' was detected at '%s[%s]' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), i);
                final Object res = getPropertySerializer(entry.getType()).deserialize(o);
                if (isValid(entry, (T) res)) {
                    o = res;
                    list.set(i, entry.getType().cast(o));
                } else {
                    Logging.warning("Invalid value '%s' at '%s[%s]'!", obj, entry.getName(), i);
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public <T> Map<String, T> get(MappedProperty<T> entry) {
        Object obj = null;
        try {
            obj = getEntryValue(entry);
        } catch (IllegalArgumentException e) {
            throw (IllegalArgumentException) e.fillInStackTrace();
        }
        if (obj == null) {
            return null;
        }
        if (obj instanceof ConfigurationSection) {
            obj = ((ConfigurationSection) obj).getValues(false);
        }
        if (!(obj instanceof Map)) {
            obj = new HashMap<String, Object>();
        }
        Map<String, Object> map = (Map<String, Object>) obj;
        for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
            Object o = mapEntry.getValue();
            if (!entry.getType().isInstance(o)) {
                Logging.fine("An invalid value of '%s' was detected at '%s%s%s' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), getConfigOptions().pathSeparator(), mapEntry.getKey());
                final Object res = getPropertySerializer(entry.getType()).deserialize(o);
                if (isValid(entry, (T) res)) {
                    o = res;
                    map.put(mapEntry.getKey(), entry.getType().cast(o));
                } else {
                    Logging.warning("Invalid value '%s' at '%s%s%s'!", obj, entry.getName() + getConfigOptions().pathSeparator() + mapEntry.getKey());
                    continue;
                }
            }
        }
        return (Map<String, T>) map;
    }

    @Override
    public <T> T get(MappedProperty<T> entry, String key) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        final String path = entry.getName() + getConfigOptions().pathSeparator() + key;
        Object obj = getConfig().get(path);
        if (obj == null) {
            return null;
        }
        if (!entry.getType().isInstance(obj)) {
            Logging.fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, path);
            final Object res = getPropertySerializer(entry.getType()).deserialize(obj);
            if (isValid(entry, (T) res)) {
                obj = res;
            }
        }
        return entry.getType().cast(obj);
    }

    @Override
    public NestedProperties get(NestedProperty entry) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        return this.nestMap.get(entry);
    }

    @Override
    public <T> boolean set(SimpleProperty<T> entry, T value) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        if (value == null) {
            getConfig().set(entry.getName(), null);
            changed(entry);
            return true;
        }
        if (!isValid(entry, value)) {
            return false;
        }
        getConfig().set(entry.getName(), value);
        changed(entry);
        return true;
    }

    @Override
    public <T> boolean set(ListProperty<T> entry, List<T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    @Override
    public <T> boolean set(MappedProperty<T> entry, Map<String, T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
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
        getConfig().set(entry.getName() + getConfigOptions().pathSeparator() + key, value);
        changed(entry);
        return true;
    }

}
