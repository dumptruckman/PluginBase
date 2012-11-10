/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
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

    protected final BukkitPlugin plugin;
    protected final CommentedYamlConfiguration config;

    private final Map<NestedProperty, NestedYamlProperties> nestMap = new HashMap<NestedProperty, NestedYamlProperties>();

    public AbstractYamlProperties(final BukkitPlugin plugin,
                                  final CommentedYamlConfiguration config,
                                  final Class... configClasses) {
        super(configClasses);
        if (plugin == null) {
            throw new IllegalArgumentException("plugin may not be null!");
        }
        this.plugin = plugin;
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
                                Object obj = section.get(key);
                                if (obj != null) {
                                    if (valueProperty.isValid(obj)) {
                                        Object res = getPropertySerializer(valueProperty.getType()).deserialize(obj);
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
                            List newList = new ArrayList(list.size());
                            for (int i = 0; i < list.size(); i++) {
                                Object obj = list.get(i);
                                if (valueProperty.isValid(obj)) {
                                    Object res = getPropertySerializer(valueProperty.getType()).deserialize(obj);
                                    newList.add(res);
                                } else {
                                    Logging.warning("Invalid value '" + obj + "' at '" + valueProperty.getName() + "[" + i + "]'.  Value will be deleted!");
                                }
                            }
                            getConfig().set(valueProperty.getName(), newList);
                        }
                    } else if (valueProperty instanceof SimpleProperty && !valueProperty.getType().isAssignableFrom(Null.class)) {
                        Object obj = getConfig().get(valueProperty.getName());
                        if (obj == null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            if (valueProperty.isValid(obj)) {
                                Object res = getPropertySerializer(valueProperty.getType()).deserialize(obj);
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
                final NestedYamlProperties nestedProperties = new NestedYamlProperties(plugin, config, this,
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
        Object obj = getEntryValue(entry);
        if (obj == null) {
            return null;
        }
        if (!entry.getType().isInstance(obj)) {
            Logging.fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, entry.getName());
            if (entry.isValid(obj)) {
                obj = getPropertySerializer(entry.getType()).deserialize(obj);
            }
        }
        return entry.getType().cast(obj);
    }

    @Override
    public <T> List<T> get(ListProperty<T> entry) throws IllegalArgumentException {
        Object obj = getEntryValue(entry);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List)) {
            obj = new ArrayList<Object>();
        }
        List list = (List) obj;
        List<T> resultList = entry.getNewTypeList();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (!entry.getType().isInstance(o)) {
                Logging.fine("An invalid value of '%s' was detected at '%s[%s]' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), i);
                if (entry.isValid(o)) {
                    o = getPropertySerializer(entry.getType()).deserialize(o);
                } else {
                    Logging.warning("Invalid value '%s' at '%s[%s]'!", obj, entry.getName(), i);
                    continue;
                }
            }
            resultList.add(entry.getType().cast(o));
        }
        return resultList;
    }

    @Override
    public <T> Map<String, T> get(MappedProperty<T> entry) throws IllegalArgumentException {
        Object obj = getEntryValue(entry);
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
        Map<String, T> resultMap = entry.getNewTypeMap();
        for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
            Object o = mapEntry.getValue();
            if (!entry.getType().isInstance(o)) {
                        Logging.fine("An invalid value of '%s' was detected at '%s%s%s' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), getConfigOptions().pathSeparator(), mapEntry.getKey());
                if (entry.isValid(o)) {
                    o = getPropertySerializer(entry.getType()).deserialize(o);
                } else {
                    Logging.warning("Invalid value '%s' at '%s%s%s'!", obj, entry.getName() + getConfigOptions().pathSeparator() + mapEntry.getKey());
                    continue;
                }
            }
            resultMap.put(mapEntry.getKey(), entry.getType().cast(o));
        }
        return resultMap;
    }

    @Override
    public <T> T get(MappedProperty<T> entry, String key) throws IllegalArgumentException {
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
            if (entry.isValid(obj)) {
                obj = getPropertySerializer(entry.getType()).deserialize(obj);
            }
        }
        return entry.getType().cast(obj);
    }

    @Override
    public NestedProperties get(NestedProperty entry) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        return this.nestMap.get(entry);
    }

    @Override
    public <T> boolean set(SimpleProperty<T> entry, T value) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        if (value == null) {
            getConfig().set(entry.getName(), null);
            return true;
        }
        if (!entry.isValid(value)) {
            return false;
        }
        getConfig().set(entry.getName(), value);
        return true;
    }

    @Override
    public <T> boolean set(ListProperty<T> entry, List<T> newValue) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
        return true;
    }

    @Override
    public <T> boolean set(MappedProperty<T> entry, Map<String, T> newValue) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
        return true;
    }

    @Override
    public <T> boolean set(MappedProperty<T> entry, String key, T value) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName() + getConfigOptions().pathSeparator() + key, value);
        return true;
    }


}
