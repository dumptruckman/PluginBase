package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Null;

import java.util.List;

class DefaultNullProperty extends DefaultValueProperty<Null> implements NullProperty {

    public DefaultNullProperty(String path, List<String> comments, List<String> aliases,
                               PropertySerializer<Null> serializer, PropertyValidator validator, Message description,
                               boolean deprecated, boolean defaultIfMissing) {
        super(Null.class, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
    }

    @Override
    public Object getDefault() {
        return null;
    }
}
