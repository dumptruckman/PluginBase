package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlConfig implements IConfig {

    private CommentedYamlConfiguration config;
    private BukkitPlugin plugin;
    private Entries entries;
    
    public AbstractYamlConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends IConfig>... configClasses) throws IOException {
        if (configFile.isDirectory()) {
            throw new IllegalArgumentException("configFile may NOT be directory!");
        }
        if (!configFile.getName().endsWith(".yml")) {
            throw new IllegalArgumentException("configFile MUST be yaml!");
        }
        entries = new Entries(configClasses);
        this.plugin = plugin;
        // Make the data folders
        if (configFile.getParentFile().mkdirs()) {
            Logging.fine("Created folder for config file.");
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
        this.setDefaults();
        
        config.getConfig().options().header(getHeader());

        // Saves the configuration from memory to file
        config.save();
    }

    /**
     * Loads default settings for any missing config values.
     */
    private void setDefaults() {
        for (ConfigEntry path : entries.entries) {
            config.addComment(path.getName(), path.getComments());
            if (getConfig().get(path.getName()) == null) {
                if (path.getDefault() != null) {
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
    
    public <T> T get(ConfigEntry<T> entry) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("ConfigEntry not registered to this config!");
        }
        T t = entry.deserialize(getConfig().get(entry.getName()));
        if (!isValid(entry, t)) {
            return entry.getDefault();
        }
        return t;
    }

    @Override
    public <T> boolean set(ConfigEntry<T> entry, T newValue) throws IllegalArgumentException {
        if (!isInConfig(entry)) {
            throw new IllegalArgumentException("ConfigEntry not registered to this config!");
        }
        if (!entry.isValid(newValue)) {
            return false;
        }
        getConfig().set(entry.getName(), entry.serialize(newValue));
        return true;
    }

    protected Configuration getConfig() {
        return this.config.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        this.config.save();
    }

    protected abstract ConfigEntry getSettingsEntry();
    
    protected abstract String getHeader();

    private final class Entries {

        private final Set<ConfigEntry> entries = new HashSet<ConfigEntry>();
        
        private Entries(Class<? extends IConfig>... configClasses) {
            Set<Class> classes = new HashSet<Class>();
            for (Class configClass : configClasses) {
                classes.add(configClass);
                for (Class clazz : configClass.getInterfaces()) {
                    classes.add(clazz);
                }
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
