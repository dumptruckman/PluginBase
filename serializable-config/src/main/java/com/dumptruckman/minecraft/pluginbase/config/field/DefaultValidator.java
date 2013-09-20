package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.Validator;

import java.beans.PropertyVetoException;

class DefaultValidator implements Validator {

    @Override
    public Object validateChange(Object newValue, Object oldValue) throws PropertyVetoException {
        return newValue;
    }
}
