package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlConfig implements BaseConfig {

    /*static {
        Entries.registerConfig(BaseConfig.class);
    }*/

    private CommentedYamlConfiguration config;
    private BukkitPlugin plugin;
    
    public AbstractYamlConfig(BukkitPlugin plugin) throws IOException {
        Entries.registerConfig(BaseConfig.class);
        this.plugin = plugin;
        // Make the data folders
        if (this.plugin.getDataFolder().mkdirs()) {
            Logging.fine("Created data folder.");
        }

        // Check if the config file exists.  If not, create it.
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                Logging.fine("Created config file.");
            }
        }

        // Load the configuration file into memory
        config = new CommentedYamlConfiguration(configFile);
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
        for (ConfigEntry path : Entries.entries) {
            config.addComment(path.getName(), path.getComments());
            if (getConfig().get(path.getName()) == null) {
                if (path.getDefault() != null) {
                    Logging.fine("Config: Defaulting '" + path.getName() + "' to " + path.getDefault());
                    getConfig().set(path.getName(), path.getDefault());
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
    
    public Locale get(ConfigEntry<Locale> entry) {
        if (entry instanceof AdvancedConfigEntry) {
            Object o = getConfig().get(entry.getName());
            if (!isValid(entry, o)) {
                return get(entry);
            }
            return (Locale) ((AdvancedConfigEntry) entry).convertForGet(o.toString());
        } else {
            return new Locale(entry.getDefault().toString());
        }
    }

    public Boolean get(ConfigEntry<Boolean> entry) {
        Object o = getConfig().get(entry.getName());
        if (!isValid(entry, o)) {
            return get(entry);
        }
        if (entry instanceof AdvancedConfigEntry) {
            return (Boolean) ((AdvancedConfigEntry) entry).convertForGet(o.toString());
        } else {
            return getConfig().getBoolean(entry.getName());
        }
    }

    public Integer get(ConfigEntry<Integer> entry) {
        Object o = getConfig().get(entry.getName());
        if (!isValid(entry, o)) {
            return get(entry);
        }
        if (entry instanceof AdvancedConfigEntry) {
            return (Integer) ((AdvancedConfigEntry) entry).convertForGet(o.toString());
        } else {
            return getConfig().getInt(entry.getName());
        }
    }



    @Override
    public String get(ConfigEntry<String> entry) {
        Object o = getConfig().get(entry.getName());
        if (!isValid(entry, o)) {
            return get(entry);
        }
        if (entry instanceof AdvancedConfigEntry) {
            return (String) ((AdvancedConfigEntry) entry).convertForGet(o.toString());
        } else {
            return getConfig().getString(entry.getName());
        }
    }

    @Override
    public List get(ConfigEntry<List<String>> entry) {
        Object o = getConfig().get(entry.getName());
        if (!isValid(entry, o)) {
            return get(entry);
        }
        if (entry instanceof AdvancedConfigEntry) {
            return (List<String>) ((AdvancedConfigEntry) entry).convertForGet(o.toString());
        } else {
            return getConfig().getStringList(entry.getName());
        }
    }

    @Override
    public boolean set(ConfigEntry entry, Object newValue) {
        if (!entry.isValid(newValue)) {
            return false;
        }
        if (entry instanceof AdvancedConfigEntry) {
            getConfig().set(entry.getName(), ((AdvancedConfigEntry) entry).convertForSet(newValue));
        } else {
            getConfig().set(entry.getName(), newValue);
        }
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
/*
    public Object get(ConfigEntry entry) {
        Object result = getConfig().get(entry.getName());
        if (!entry.getType().isInstance(result)) {
            Logging.warning("Config value: " + entry.getName() + " is not valid.");
            Logging.warning("Setting to default value: " + entry.getDefault());
            result = entry.getDefault();
            set(entry, result);
        }
        return result;
    }
    public void set(ConfigEntry entry, Object newValue) {
        if (entry.getType().isInstance(newValue)) {
            getConfig().set(entry.getName(), newValue);
        } else {
            throw new IllegalArgumentException("newValue is not correct type for " + entry.getName());
        }
    }
    */

    protected abstract ConfigEntry getSettingsEntry();
    
    protected abstract String getHeader();
}
