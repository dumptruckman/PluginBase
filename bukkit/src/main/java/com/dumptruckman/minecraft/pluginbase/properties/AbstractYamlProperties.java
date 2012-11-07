/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.dumptruckman.minecraft.pluginbase.util.Null;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlProperties implements Properties {

    private CommentedYamlConfiguration config;
    private final File configFile;
    private final BukkitPlugin plugin;
    private final Entries entries;
    private final boolean doComments;
    private final boolean autoDefaults;

    public AbstractYamlProperties(BukkitPlugin plugin, boolean doComments, boolean autoDefaults, File configFile, Class... configClasses) throws IOException {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin may not be null!");
        }
        if (configFile == null) {
            throw new IllegalArgumentException("configFile may not be null!");
        }
        if (configFile.isDirectory()) {
            throw new IllegalArgumentException("configFile may NOT be directory!");
        }
        if (!configFile.getName().endsWith(".yml")) {
            throw new IllegalArgumentException("configFile MUST be yaml!");
        }
        this.configFile = configFile;
        this.doComments = doComments;
        this.autoDefaults = autoDefaults;
        this.entries = new Entries(configClasses);
        this.plugin = plugin;

        // Make the data folders
        if (configFile.getParent() != null) {
            if (configFile.getParentFile().mkdirs()) {
                Logging.fine("Created folder for config file.");
            }
        }

        // Check if the config file exists.  If not, create it.
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                Logging.fine("Created config file: %s", configFile.getAbsolutePath());
            }
        }
        // Load the configuration file into memory
        config = new CommentedYamlConfiguration(configFile, doComments);
        load();

        // Saves the configuration from memory to file
        save();
    }

    private void load() throws IOException {
        config.load();
        deserializeAll();

        // Sets defaults config values
        if (autoDefaults) {
            this.setDefaults();
        }

        config.getConfig().options().header(getHeader());
    }

    @Override
    public void reload() throws Exception {
        load();
    }

    private void deserializeAll() {
        Logging.finer("Beginning deserialization...");
        for (Property property : entries.properties) {
            if (getConfig().get(property.getName()) != null) {
                if (property instanceof MappedProperty) {
                    ConfigurationSection section = getConfig().getConfigurationSection(property.getName());
                    if (section == null) {
                        getConfig().set(property.getName(), property.getDefault());
                    } else {
                        for (String key : section.getKeys(false)) {
                            Object obj = section.get(key);
                            if (obj != null) {
                                if (property.isValid(obj)) {
                                    Object res = property.deserialize(obj);
                                    section.set(key, res);
                                } else {
                                    Logging.warning("Invalid value '" + obj + "' at '" + property.getName() + getConfig().options().pathSeparator() + key + "'.  Value will be deleted!");
                                    section.set(key, null);
                                }
                            }
                        }
                    }
                } else if (property instanceof ListProperty) {
                    List list = getConfig().getList(property.getName());
                    if (list == null) {
                        getConfig().set(property.getName(), property.getDefault());
                    } else {
                        List newList = new ArrayList(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            Object obj = list.get(i);
                            if (property.isValid(obj)) {
                                Object res = property.deserialize(obj);
                                newList.add(res);
                            } else {
                                Logging.warning("Invalid value '" + obj + "' at '" + property.getName() + "[" + i + "]'.  Value will be deleted!");
                            }
                        }
                        getConfig().set(property.getName(), newList);
                    }
                } else if (property instanceof SimpleProperty && !property.getType().isAssignableFrom(Null.class)) {
                    Object obj = getConfig().get(property.getName());
                    if (obj == null) {
                        getConfig().set(property.getName(), property.getDefault());
                    } else {
                        if (property.isValid(obj)) {
                            Object res = property.deserialize(obj);
                            getConfig().set(property.getName(), res);
                        } else {
                            Logging.warning("Invalid value '" + obj + "' at '" + property.getName() + "'.  Value will be defaulted!");
                            getConfig().set(property.getName(), property.getDefault());
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads default settings for any missing config values.
     */
    private void setDefaults() {
        for (Property path : entries.properties) {
            //config.addComment(path.getName(), path.getComments());
            if (getConfig().get(path.getName()) == null) {
                if (path.isDeprecated()) {
                    continue;
                }
                if (path instanceof MappedProperty) {
                    Logging.fine("Config: Defaulting map for '%s'", path.getName());
                    if (path.getDefault() != null) {
                        getConfig().set(path.getName(), path.getDefault());
                    } else {
                        getConfig().set(path.getName(), ((MappedProperty) path).getNewTypeMap());
                    }
                } else if (path instanceof ListProperty) {
                    ListProperty listPath = (ListProperty) path;
                    Logging.fine("Config: Defaulting list for '%s'", path.getName());
                    if (listPath.getDefault() != null) {
                        getConfig().set(path.getName(), listPath.getDefault());
                    } else {
                        getConfig().set(path.getName(), listPath.getNewTypeList());
                    }
                } else if (path.getDefault() != null) {
                    Logging.fine("Config: Defaulting '%s' to %s", path.getName(), path.getDefault());
                    getConfig().set(path.getName(), path.getDefault());
                }
            }
        }
    }

    private boolean isValid(Property property, Object o) {
        if (!property.isValid(o)) {
            Logging.warning(property.getName() + " contains an invalid value!");
            Logging.warning(plugin.getMessager().getMessage(property.getInvalidMessage()));
            Logging.warning("Setting to default of: " + property.getDefault());
            getConfig().set(property.getName(), property.getDefault());
            save();
            return false;
        }
        return true;
    }
    
    protected final boolean isInConfig(Property property) {
        return entries.properties.contains(property);
    }

    private Object getEntryValue(Property property) throws IllegalArgumentException {
        if (!isInConfig(property)) {
            throw new IllegalArgumentException("property not registered to this config!");
        }
        Object obj = getConfig().get(property.getName());
        if (obj == null) {
            if (property.shouldDefaultIfMissing()) {
                obj = property.getDefault();
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
                obj = entry.deserialize(obj);
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
                    o = entry.deserialize(o);
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
                        Logging.fine("An invalid value of '%s' was detected at '%s%s%s' during a get call.  Attempting to deserialize and replace...", o, entry.getName(), getConfig().options().pathSeparator(), mapEntry.getKey());
                if (entry.isValid(o)) {
                    o = entry.deserialize(o);
                } else {
                    Logging.warning("Invalid value '%s' at '%s%s%s'!", obj, entry.getName() + getConfig().options().pathSeparator() + mapEntry.getKey());
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
        final String path = entry.getName() + getConfig().options().pathSeparator() + key;
        Object obj = getConfig().get(path);
        if (obj == null) {
            return null;
        }
        if (!entry.getType().isInstance(obj)) {
            Logging.fine("An invalid value of '%s' was detected at '%s' during a get call.  Attempting to deserialize and replace...", obj, path);
            if (entry.isValid(obj)) {
                obj = entry.deserialize(obj);
            }
        }
        return entry.getType().cast(obj);
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
        getConfig().set(entry.getName() + getConfig().options().pathSeparator() + key, value);
        return true;
    }

    protected Configuration getConfig() {
        return this.config.getConfig();
    }

    private void serializeAll(FileConfiguration newConfig) {
        for (Property property : entries.properties) {
            if (getConfig().get(property.getName()) != null) {
                if (property instanceof MappedProperty) {
                    Object o = getConfig().get(property.getName());
                    if (o == null) {
                        Logging.fine("Missing property: %s", property.getName());
                        continue;
                    }
                    Map map;
                    if (o instanceof ConfigurationSection) {
                        map = ((ConfigurationSection) o).getValues(false);
                    } else if (!(o instanceof Map)) {
                        Logging.fine("Missing property: %s", property.getName());
                        continue;
                    } else {
                        map = (Map) o;
                    }
                    for (Object key : map.keySet()) {
                        Object obj = map.get(key);
                        if (property.getType().isInstance(obj)) {
                            if (obj != null) {
                                map.put(key, property.serialize(property.getType().cast(obj)));
                            }
                        } else {
                            Logging.warning("Could not serialize: %s", property.getName());
                        }
                    }
                    newConfig.set(property.getName(), map);
                } else if (property instanceof ListProperty) {
                    List list = getConfig().getList(property.getName());
                    if (list == null) {
                        Logging.fine("Missing property: %s", property.getName());
                        continue;
                    }
                    List newList = new ArrayList(list.size());
                    for (Object obj : list) {
                        if (property.getType().isInstance(obj)) {
                            if (obj != null) {
                                newList.add(property.serialize(property.getType().cast(obj)));
                            }
                        } else {
                            Logging.warning("Could not serialize: %s", property.getName());
                        }
                    }
                    newConfig.set(property.getName(), newList);
                } else if (property instanceof SimpleProperty && !property.getType().isAssignableFrom(Null.class)) {
                    Object obj = getConfig().get(property.getName());
                    if (obj == null) {
                        Logging.fine("Missing property: %s", property.getName());
                        continue;
                    }
                    if (property.getType().isInstance(obj)) {
                        Object res = property.serialize(property.getType().cast(obj));
                        newConfig.set(property.getName(), res);
                    } else {
                        Logging.warning("Could not serialize '%s' since value is '%s' instead of '%s'", property.getName(), obj.getClass(), property.getType());
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        CommentedYamlConfiguration newConfig = new CommentedYamlConfiguration(configFile, doComments);
        newConfig.newConfig();
        newConfig.getConfig().options().header(getHeader());
        serializeAll(newConfig.getConfig());
        if (doComments) {
            for (Property path : entries.properties) {
                newConfig.addComment(path.getName(), path.getComments());
            }
        }
        newConfig.save();
    }
    
    protected String getHeader() {
        return "";
    }

    private final class Entries {

        private final Set<Property> properties = new CopyOnWriteArraySet<Property>();
        
        private Entries(Class... configClasses) {
            final Set<Class> classes = new LinkedHashSet<Class>(10);
            for (Class configClass : configClasses) {
                classes.add(configClass);
                classes.addAll(Arrays.asList(configClass.getInterfaces()));
                if (configClass.getSuperclass() != null) {
                    classes.add(configClass.getSuperclass());
                }
            }
            for (Class clazz : classes) {
                final Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    try {
                        if (Property.class.isInstance(field.get(null))) {
                            try {
                                properties.add((Property) field.get(null));
                            } catch(IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IllegalArgumentException ignore) {
                    } catch (IllegalAccessException ignore) {
                    } catch (NullPointerException ignore) { }
                }
            }
        }
    }
}
