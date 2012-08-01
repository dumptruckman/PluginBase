package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlConfig<C> implements Config {

    private CommentedYamlConfiguration config;
    private BukkitPlugin plugin;
    private Entries entries;
    private boolean doComments;

    public AbstractYamlConfig(BukkitPlugin plugin, boolean doComments, boolean autoDefaults, File configFile, Class<? extends C>... configClasses) throws IOException {
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
        this.doComments = doComments;
        entries = new Entries(configClasses);
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
                Logging.fine("Created config file: " + configFile.getAbsolutePath());
            }
        }

        // Load the configuration file into memory
        config = new CommentedYamlConfiguration(configFile, doComments);
        config.load();

        // Sets defaults config values
        if (autoDefaults) {
            this.setDefaults();
        }
        
        config.getConfig().options().header(getHeader());

        // Saves the configuration from memory to file
        save();
    }

    /**
     * Loads default settings for any missing config values.
     */
    private void setDefaults() {
        for (ConfigEntry path : entries.entries) {
            //config.addComment(path.getName(), path.getComments());
            if (getConfig().get(path.getName()) == null) {
                if (path.isDeprecated()) {
                    continue;
                }
                if (path instanceof MappedConfigEntry) {
                    Logging.fine("Config: Defaulting map for '" + path.getName() + "'");
                    getConfig().set(path.getName(), ((MappedConfigEntry) path).getNewTypeMap());
                } else if (path instanceof ListConfigEntry) {
                    ListConfigEntry listPath = (ListConfigEntry) path;
                    Logging.fine("Config: Defaulting list for '" + path.getName() + "'");
                    if (listPath.getDefaultList() != null) {
                        getConfig().set(path.getName(), listPath.getDefaultList());
                    } else {
                        getConfig().set(path.getName(), listPath.getNewTypeList());
                    }
                } else if (path.getDefault() != null) {
                    Logging.fine("Config: Defaulting '" + path.getName() + "' to " + path.serialize(path.getDefault()));
                    getConfig().set(path.getName(), path.serialize(path.getDefault()));
                }
            }
        }
    }

    private boolean isValid(ConfigEntry entry, Object o) {
        if (!entry.isValid(o)) {
            Logging.warning(entry.getName() + " contains an invalid value!");
            Logging.warning(plugin.getMessager().getMessage(entry.getInvalidMessage()));
            Logging.warning("Setting to default of: " + entry.getDefault());
            getConfig().set(entry.getName(), entry.getDefault());
            save();
            return false;
        }
        return true;
    }
    
    protected final boolean isInConfig(ConfigEntry entry) {
        return entries.entries.contains(entry);
    }

    private Object getEntryValue(ConfigEntry entry) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("entry not registered to this config!");
        }
        Object obj = getConfig().get(entry.getName());
        if (obj == null) {
            if (entry.shouldDefaultIfMissing()) {
                obj = entry.getDefault();
            }
        }
        return obj;
    }

    @Override
    public <T> T get(SimpleConfigEntry<T> entry) throws IllegalArgumentException {
        Object obj = getEntryValue(entry);
        if (obj == null) {
            return null;
        }
        T t = entry.deserialize(obj);
        if (!isValid(entry, t)) {
            return entry.getDefault();
        }
        return t;
    }

    @Override
    public <T> T get(ListConfigEntry<T> entry, int index) throws IllegalArgumentException {
        Object obj = getEntryValue(entry);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List)) {
            obj = new ArrayList<Object>();
        }
        List<Object> list = (List<Object>) obj;
        Object res = list.get(index);
        if (res == null) {
            return null;
        }
        if (!entry.isValid(res)) {
            return entry.getDefault();
        }
        return entry.deserialize(res);
    }

    @Override
    public <T> List<T> get(ListConfigEntry<T> entry) throws IllegalArgumentException {
        Object obj = getEntryValue(entry);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof List)) {
            obj = new ArrayList<Object>();
        }
        List<Object> list = (List<Object>) obj;
        List<T> resultList = entry.getNewTypeList();
        for (Object o : list) {
            resultList.add(entry.deserialize(o));
        }
        return resultList;
    }

    @Override
    public <T> T get(MappedConfigEntry<T> entry, String key) throws IllegalArgumentException {
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
        Object res = map.get(key);
        if (res == null) {
            return null;
        }
        if (!entry.isValid(res)) {
            return entry.getDefault();
        }
        return entry.deserialize(res);
    }

    @Override
    public <T> Map<String, T> get(MappedConfigEntry<T> entry) throws IllegalArgumentException {
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
            resultMap.put(mapEntry.getKey(), entry.deserialize(mapEntry.getValue()));
        }
        return resultMap;
    }

    @Override
    public <T> boolean set(SimpleConfigEntry<T> entry, T value) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("ConfigEntry not registered to this config!");
        }
        if (value == null) {
            getConfig().set(entry.getName(), null);
            return true;
        }
        if (!entry.isValid(value)) {
            return false;
        }
        getConfig().set(entry.getName(), entry.serialize(value));
        return true;
    }
/*
    @Override
    public <T> boolean set(ListConfigEntry<T> entry, int index, T value) throws IllegalArgumentException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
*/
    @Override
    public <T> boolean set(ListConfigEntry<T> entry, List<T> newValue) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("ConfigEntry not registered to this config!");
        }
        List<Object> resultList = new LinkedList<Object>();
        for (T t : newValue) {
            if (!entry.isValid(t)) {
                return false;
            }
            resultList.add(entry.serialize(t));
        }
        getConfig().set(entry.getName(), resultList);
        return true;
    }

    @Override
    public <T> boolean set(MappedConfigEntry<T> entry, String key, T value) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("ConfigEntry not registered to this config!");
        }
        if (value == null) {
            getConfig().set(entry.getName() + "." + key, null);
            return true;
        }
        if (!entry.isValid(value)) {
            return false;
        }
        getConfig().set(entry.getName() + "." + key, entry.serialize(value));
        return true;
    }
/*
    @Override
    public <T> boolean set(MappedConfigEntry<T> entry, Map<String, T> value) throws IllegalArgumentException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
*/

    protected Configuration getConfig() {
        return this.config.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        if (doComments) {
            for (ConfigEntry path : entries.entries) {
                config.addComment(path.getName(), path.getComments());
            }
        }
        this.config.save();
    }
    
    protected String getHeader() {
        return "";
    }

    private final class Entries {

        private final Set<ConfigEntry> entries = new HashSet<ConfigEntry>();
        
        private Entries(Class<? extends C>... configClasses) {
            Set<Class> classes = new HashSet<Class>();
            for (Class configClass : configClasses) {
                classes.add(configClass);
                classes.addAll(Arrays.asList(configClass.getInterfaces()));
                if (configClass.getSuperclass() != null) {
                    classes.add(configClass.getSuperclass());
                }
            }
            for (Class clazz : classes) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    try {
                        if (ConfigEntry.class.isInstance(field.get(null))) {
                            try {
    
                                entries.add((ConfigEntry) field.get(null));
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
