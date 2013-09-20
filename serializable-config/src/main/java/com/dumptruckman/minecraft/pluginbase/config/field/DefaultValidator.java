package com.dumptruckman.minecraft.pluginbase.config.field;

import java.beans.PropertyVetoException;

class DefaultValidator implements Validator {

    @Override
    public Object validateChange(Object newValue, Object oldValue) throws PropertyVetoException {
        return newValue;
    }
}
