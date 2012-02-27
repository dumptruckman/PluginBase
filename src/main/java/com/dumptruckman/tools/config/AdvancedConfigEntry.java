package com.dumptruckman.tools.config;

public abstract class AdvancedConfigEntry<T, K> extends SimpleConfigEntry<T> {

    public AdvancedConfigEntry(String path, K def, String... comments) {
        super(path, def, comments);
    }
/*
    @Override
    public T get() {
        if (!isPluginSet()) {
            Logging.finest("Retrieved default for '" + getName() + "'");
            return convertForGet((K) getDefault());
        }
        Object result = plugin.config().get(this);
        if (getType().isInstance(result)) {
            return convertForGet((K) result);
        } else {
            throw new IllegalArgumentException(getType() + " is not supported by loaded config!");
        }
    }

    @Override
    public synchronized void set(T value) {
        if (!isPluginSet()) {
            Logging.finest("Cannot set values when Config is unitialized");
            return;
        }
        plugin.config().set(this, convertForSet(value));
    }*/

    public abstract K convertForSet(T t);
    
    public abstract T convertForGet(K k);
}
