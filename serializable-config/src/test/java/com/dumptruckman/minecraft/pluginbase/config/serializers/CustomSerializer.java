package com.dumptruckman.minecraft.pluginbase.config.serializers;

import com.dumptruckman.minecraft.pluginbase.config.examples.Custom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomSerializer implements Serializer<Custom> {

    @Nullable
    @Override
    public Object serialize(@Nullable Custom object) {
        return object != null ? serializeRegisteredType(object) : null;
    }

    @NotNull
    @Override
    public Map<String, Object> serializeRegisteredType(@NotNull Custom object) {
        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put("name", object.name);
        return result;
    }

    @NotNull
    @Override
    public Custom deserialize(@NotNull Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        if (serialized instanceof Map) {
            return deserializeToObject((Map) serialized, new Custom(""));
        } else {
            throw new IllegalArgumentException("serialized form must be a map");
        }
    }

    @Override
    public Custom deserializeToObject(@NotNull Map data, @NotNull Custom object) {
        return new Custom(data.get("name").toString());
    }
}
