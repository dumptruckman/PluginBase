/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.properties;

import pluginbase.logging.PluginLogger;
import pluginbase.properties.AbstractProperties;
import pluginbase.properties.ListProperty;
import pluginbase.properties.MappedProperty;
import pluginbase.properties.NestedProperties;
import pluginbase.properties.NestedProperty;
import pluginbase.properties.Null;
import pluginbase.properties.Properties;
import pluginbase.properties.Property;
import pluginbase.properties.SimpleProperty;
import pluginbase.properties.ValueProperty;
import pluginbase.properties.serializers.DefaultSerializer;
import pluginbase.properties.serializers.DefaultStringSerializer;
import pluginbase.properties.serializers.StringStringSerializer;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Properties implementation that stores Property values into a {@link FileConfiguration}.
 */
public abstract class AbstractFileProperties extends AbstractProperties implements Properties {

    /** The file configuration object that backs this Properties object. */
    @NotNull
    protected final FileConfiguration config;

    private final Map<NestedProperty, NestedFileProperties> nestMap = new HashMap<NestedProperty, NestedFileProperties>();

    /**
     * Constructs a new Properties object using the given config to store Property values in.
     *
     * @param logger a logger to use for any important messages this Properties object may need to log.
     * @param config the persistence object.
     * @param configClasses the classes that declare what {@link Property} objects belong to this Properties object.
     */
    protected AbstractFileProperties(@NotNull final PluginLogger logger,
                                     @NotNull final FileConfiguration config,
                                     @NotNull final Class... configClasses) {
        super(logger, configClasses);
        this.config = config;

        for (final Property property : getProperties()) {
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

    /**
     * Gets the config object backing this Properties object.
     * <p/>
     * Should be overridden in NestedProperties to give the appropriate ConfigurationSection for the Properties sub
     * path of the backing Configuration object.
     *
     * @return the config object backing this Properties object.
     */
    @NotNull
    protected ConfigurationSection getConfig() {
        return this.config;
    }

    /**
     * Gets the configuration options for the backing file configuration.
     *
     * @return the configuration options for the backing file configuration.
     */
    @NotNull
    protected final ConfigurationOptions getConfigOptions() {
        return this.config.options();
    }

    /**
     * The name of this Properties object which is used to define the path it holds in the Configuration object.
     * <p/>
     * Should be overridden in NestedProperties to indicate the sub path this Properties object has in the
     * backing Configuration object.
     *
     * @return the name of this Properties object which is used to define the path it holds in the Configuration object.
     */
    @NotNull
    protected String getName() {
        return "";
    }

    /**
     * Tells this AbstractFileProperties to associate the comments for its Property objects with the key associated with
     * the Property's value in the backing Configuration object.
     * <p/>
     * This is typically done right before saving the Configuration to disk.
     *
     * @param config The backing configuration object to apply comments to.
     */
    protected final void doComments(@NotNull final CommentedFile config) {
        for (final Property property : getProperties()) {
            final String path;
            if (!getName().isEmpty()) {
                path = getName() + getConfigOptions().pathSeparator() + property.getName();
            } else {
                path = property.getName();
            }
            config.addComments(path, property.getComments());
            if (property instanceof NestedProperty) {
                NestedFileProperties nestedProperties = this.nestMap.get(property);
                if (nestedProperties != null) {
                    nestedProperties.doComments(config);
                }
            }
        }
    }

    /**
     * Deserializes all the values in the backing Configuration object.
     * <p/>
     * This causes the Configuration object to hold references to the actual objects each Property represents instead
     * of references to serialized form.
     * <p/>
     * If this is overridden this super method should probably still be called.
     */
    protected void deserializeAll() {
        for (final Property property : getProperties()) {
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
                                        getLog().warning("Invalid value '" + obj + "' at '" + valueProperty.getName() + getConfigOptions().pathSeparator() + key + "'.  Value will be deleted!");
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
                                    getLog().warning("Invalid value '" + res + "' at '" + valueProperty.getName() + "[" + i + "]'.  Value will be deleted!");
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
                                getLog().warning("Invalid value '" + obj + "' at '" + valueProperty.getName() + "'.  Value will be defaulted!");
                                getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                            }
                        }
                    }
                }
            } else if (property instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) property;
                final NestedFileProperties nestedProperties = new NestedFileProperties(getLog(), config, this,
                        nestedProperty.getName(), nestedProperty.getType());
                nestedProperties.deserializeAll();
                getConfig().set(nestedProperty.getName(), nestedProperties.getConfig());
                this.nestMap.put(nestedProperty, nestedProperties);
            }
        }
    }

    /**
     * Serializes all the values in the backing Configuration object.
     * <p/>
     * This causes the Configuration object to hold references to the serialized form of the objects each Property
     * represents instead of references to the actual form.
     * <p/>
     * If this is overridden this super method should probably still be called.
     *
     * @param newConfig the section of the config to be serializing for.
     */
    protected void serializeAll(@NotNull final ConfigurationSection newConfig) {
        for (final Property property : getProperties()) {
            if (property instanceof ValueProperty) {
                final ValueProperty valueProperty = (ValueProperty) property;
                if (getConfig().get(valueProperty.getName()) != null) {
                    if (valueProperty instanceof MappedProperty) {
                        Object o = getConfig().get(valueProperty.getName());
                        if (o == null) {
                            getLog().fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        Map map;
                        if (o instanceof ConfigurationSection) {
                            map = ((ConfigurationSection) o).getValues(false);
                        } else if (!(o instanceof Map)) {
                            getLog().fine("Missing property: %s", valueProperty.getName());
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
                                getLog().warning("Could not serialize: %s", valueProperty.getName());
                            }
                        }
                        newConfig.set(valueProperty.getName(), map);
                    } else if (valueProperty instanceof ListProperty) {
                        List list = getConfig().getList(valueProperty.getName());
                        if (list == null) {
                            getLog().fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        List newList = new ArrayList(list.size());
                        for (Object obj : list) {
                            if (valueProperty.getType().isInstance(obj)) {
                                if (obj != null) {
                                    newList.add(getPropertySerializer(valueProperty.getType()).serialize(valueProperty.getType().cast(obj)));
                                }
                            } else {
                                getLog().warning("Could not serialize: %s", valueProperty.getName());
                            }
                        }
                        newConfig.set(valueProperty.getName(), newList);
                    } else if (valueProperty instanceof SimpleProperty && !valueProperty.getType().isAssignableFrom(Null.class)) {
                        Object obj = getConfig().get(valueProperty.getName());
                        if (obj == null) {
                            getLog().fine("Missing property: %s", valueProperty.getName());
                            continue;
                        }
                        if (valueProperty.getType().isInstance(obj)) {
                            Object res = getPropertySerializer(valueProperty.getType()).serialize(valueProperty.getType().cast(obj));
                            newConfig.set(valueProperty.getName(), res);
                        } else {
                            getLog().warning("Could not serialize '%s' since value is '%s' instead of '%s'", valueProperty.getName(), obj.getClass(), valueProperty.getType());
                        }
                    }
                }
            } else if (property instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) property;
                final NestedFileProperties nestedProperties = this.nestMap.get(nestedProperty);
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
     * <p/>
     * If this is overridden this super method should probably still be called.
     */
    protected void setDefaults() {
        for (Property path : getProperties()) {
            if (path instanceof ValueProperty) {
                ValueProperty valueProperty = (ValueProperty) path;
                if (getConfig().get(valueProperty.getName()) == null) {
                    if (valueProperty.isDeprecated()) {
                        continue;
                    }
                    if (valueProperty instanceof MappedProperty) {
                        getLog().fine("Config: Defaulting map for '%s'", valueProperty.getName());
                        if (valueProperty.getDefault() != null) {
                            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                        } else {
                            getConfig().set(valueProperty.getName(), ((MappedProperty) valueProperty).getNewTypeMap());
                        }
                    } else if (valueProperty instanceof ListProperty) {
                        ListProperty listPath = (ListProperty) valueProperty;
                        getLog().fine("Config: Defaulting list for '%s'", valueProperty.getName());
                        if (listPath.getDefault() != null) {
                            getConfig().set(valueProperty.getName(), listPath.getDefault());
                        } else {
                            getConfig().set(valueProperty.getName(), listPath.getNewTypeList());
                        }
                    } else if (valueProperty.getDefault() != null) {
                        getLog().fine("Config: Defaulting '%s' to %s", valueProperty.getName(), valueProperty.getDefault());
                        getConfig().set(valueProperty.getName(), valueProperty.getDefault());
                    }
                }
            } else if (path instanceof NestedProperty) {
                final NestedProperty nestedProperty = (NestedProperty) path;
                final NestedFileProperties nestedProperties = this.nestMap.get(nestedProperty);
                if (nestedProperties != null) {
                    nestedProperties.setDefaults();
                } else {
                    //TODO Warn
                }
            }
        }
    }

    @Nullable
    private Object getEntryValue(@NotNull final ValueProperty valueProperty) throws IllegalArgumentException {
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

    /** {@inheritDoc} */
    @Nullable
    @Override
    public <T> T get(@NotNull final SimpleProperty<T> entry) throws IllegalArgumentException {
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
            getLog().fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, entry.getName());
            final Object res = getPropertySerializer(entry.getType()).deserialize(obj);
            if (isValid(entry, (T) res)) {
                obj = res;
            }
        }
        return entry.getType().cast(obj);
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public <T> List<T> get(@NotNull final ListProperty<T> entry) {
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
                getLog().fine("An invalid value of '%s' was detected at '%s[%s]' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), i);
                final Object res = getPropertySerializer(entry.getType()).deserialize(o);
                if (isValid(entry, (T) res)) {
                    o = res;
                    list.set(i, entry.getType().cast(o));
                } else {
                    getLog().warning("Invalid value '%s' at '%s[%s]'!", obj, entry.getName(), i);
                    continue;
                }
            }
        }
        return list;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public <T> Map<String, T> get(@NotNull final MappedProperty<T> entry) {
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
                getLog().fine("An invalid value of '%s' was detected at '%s%s%s' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), getConfigOptions().pathSeparator(), mapEntry.getKey());
                final Object res = getPropertySerializer(entry.getType()).deserialize(o);
                if (isValid(entry, (T) res)) {
                    o = res;
                    map.put(mapEntry.getKey(), entry.getType().cast(o));
                } else {
                    getLog().warning("Invalid value '%s' at '%s%s%s'!", obj, entry.getName() + getConfigOptions().pathSeparator() + mapEntry.getKey());
                    continue;
                }
            }
        }
        return (Map<String, T>) map;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public <T> T get(@NotNull final MappedProperty<T> entry, @NotNull final String key) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        final String path = entry.getName() + getConfigOptions().pathSeparator() + key;
        Object obj = getConfig().get(path);
        if (obj == null) {
            return null;
        }
        if (!entry.getType().isInstance(obj)) {
            getLog().fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, path);
            final Object res = getPropertySerializer(entry.getType()).deserialize(obj);
            if (isValid(entry, (T) res)) {
                obj = res;
            }
        }
        return entry.getType().cast(obj);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public NestedProperties get(@NotNull final NestedProperty entry) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        return this.nestMap.get(entry);
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final SimpleProperty<T> entry, @Nullable final T value) {
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

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final ListProperty<T> entry, @Nullable final List<T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
        changed(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean set(@NotNull final MappedProperty<T> entry, @Nullable final Map<String, T> newValue) {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("Property not registered to this config!");
        }
        getConfig().set(entry.getName(), newValue);
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
        getConfig().set(entry.getName() + getConfigOptions().pathSeparator() + key, value);
        changed(entry);
        return true;
    }

}
