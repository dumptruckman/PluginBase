package com.dumptruckman.minecraft.pluginbase.properties;

public class PropertyChangeEvent<T> {

    private final ValueProperty<T> property;
    private final T oldValue;
    private final T newValue;

    private boolean denyChange = false;

    public PropertyChangeEvent(final ValueProperty<T> property, final T oldValue, final T newValue) {
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public ValueProperty<T> getProperty() {
        return property;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public void setDenyChange(boolean denyChange) {
        this.denyChange = denyChange;
    }

    public boolean getDenyChange() {
        return denyChange;
    }
}
