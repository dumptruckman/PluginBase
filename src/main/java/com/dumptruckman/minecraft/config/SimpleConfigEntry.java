package com.dumptruckman.minecraft.config;

import com.dumptruckman.minecraft.locale.Message;
import com.dumptruckman.minecraft.locale.Messages;
import com.dumptruckman.minecraft.plugin.PluginBase;
import com.dumptruckman.minecraft.util.Logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleConfigEntry<T> implements ConfigEntry<T> {

    protected static PluginBase plugin = null;

    public static void init(PluginBase plugin) {
        SimpleConfigEntry.plugin = plugin;
    }

    private String path;
    private Object def;
    private final List<String> comments;

    public SimpleConfigEntry(String path, Object def, String... comments) {
        this.path = path;
        this.def = def;
        this.comments = new ArrayList<String>(Arrays.asList(comments));
        //if (this.comments.isEmpty()) {
        //    this.comments.add("");
        //}
        Entries.entries.add(this);
    }

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    public String getName() {
        return this.path;
    }

    public Class getType() {
        return this.def.getClass();
    }

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    public Object getDefault() {
        return this.def;
    }

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    public List<String> getComments() {
        return this.comments;
    }

    public boolean isPluginSet() {
        if (plugin == null) {
            if (Logging.getDebugMode() != 3) {
                Logging.setDebugMode(3);
                Logging.fine("Enabled debug mode since Config has not been initialized with a plugin.");
                Logging.fine("All config values will return default.");
            }
            return false;
        }
        return true;
    }

    /* public synchronized T get() {
        if (!isPluginSet()) {
            Logging.finest("Retrieved default for '" + getName() + "'");
            return (T) getDefault();
        }
        Object result = plugin.config().get(this);
        if (getType().isInstance(result)) {
            return (T) result;
        } else {
            throw new IllegalArgumentException(getType() + " is not supported by loaded config!");
        }
    }*/

    /* public synchronized void set(T value) {
        if (!isPluginSet()) {
            Logging.finest("Cannot set values when Config is unitialized");
            return;
        }
        plugin.config().set(this, value);
    }*/

    @Override
    public boolean isValid(Object obj) {
        return true;
    }

    @Override
    public Message getInvalidMessage() {
        return Messages.BLANK;
    }
}
