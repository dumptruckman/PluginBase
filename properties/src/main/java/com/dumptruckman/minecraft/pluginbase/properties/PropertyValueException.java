package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;

public class PropertyValueException extends PropertyException {

    private final Object value;

    public PropertyValueException(BundledMessage b, Property property, Object value) {
        super(b, property);
        this.value = value;
    }

    public PropertyValueException(BundledMessage b, Throwable throwable, Property property, Object value) {
        super(b, throwable, property);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
