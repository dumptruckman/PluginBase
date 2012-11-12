package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messaging.PluginBaseException;

public class PropertyException extends PluginBaseException {

    private final Property property;

    public PropertyException(BundledMessage b, Property property) {
        super(b);
        this.property = property;
    }

    public PropertyException(BundledMessage b, Throwable throwable, Property property) {
        super(b, throwable);
        this.property = property;
    }

    public Property getProperty() {
        return property;
    }
}
