package com.dumptruckman.minecraft.pluginbase.config;

public abstract class AdvancedConfigEntry<T> extends SimpleConfigEntry<T> {

    public AdvancedConfigEntry(Class<T> type, String path, T def, String... comments) {
        super(type, path, def, comments);
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

    @Override
    public abstract Object serialize(T value);

    @Override
    public abstract T deserialize(Object o);
}
