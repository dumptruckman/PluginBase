package com.dumptruckman.minecraft.pluginbase.config.field;

import java.beans.PropertyVetoException;

public interface Validator {

    Object validateChange(Object newValue, Object oldValue) throws PropertyVetoException;
}
