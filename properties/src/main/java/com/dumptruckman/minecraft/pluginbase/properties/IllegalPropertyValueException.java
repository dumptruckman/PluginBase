package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.BundledMessage;

public class IllegalPropertyValueException extends PropertyValueException {

    public IllegalPropertyValueException(BundledMessage b, Property property, Object value) {
        super(b, property, value);
    }

    public IllegalPropertyValueException(BundledMessage b, Throwable throwable, Property property, Object value) {
        super(b, throwable, property, value);
    }
}
