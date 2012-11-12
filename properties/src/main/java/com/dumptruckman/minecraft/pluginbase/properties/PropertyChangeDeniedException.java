package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;

public class PropertyChangeDeniedException extends PropertyValueException {

    public PropertyChangeDeniedException(BundledMessage b, Property property, Object value) {
        super(b, property, value);
    }

    public PropertyChangeDeniedException(BundledMessage b, Throwable throwable, Property property, Object value) {
        super(b, throwable, property, value);
    }
}
