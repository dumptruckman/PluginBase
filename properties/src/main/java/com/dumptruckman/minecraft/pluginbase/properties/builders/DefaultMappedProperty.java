/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class DefaultMappedProperty<T> extends DefaultValueProperty<T> implements MappedProperty<T> {

    @NotNull
    private final Class<? extends Map> mapClass;
    @Nullable
    private final Map<String, T> defMap;

    public DefaultMappedProperty(@NotNull final Class<T> type, @NotNull final String path,
                                 @Nullable final Map<String, T> def, @NotNull final List<String> comments,
                                 @NotNull final List<String> aliases, @Nullable final PropertySerializer<T> serializer,
                                 @NotNull final PropertyValidator<T> validator, Message description,
                                 boolean deprecated, boolean defaultIfMissing,
                                 @NotNull final Class<? extends Map> mapClass) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.mapClass = mapClass;
        if (def == null) {
            this.defMap = null;
        } else {
            this.defMap = Collections.unmodifiableMap(def);
        }
    }

    @NotNull
    @Override
    public Map<String, T> getNewTypeMap() {
        try {
            @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
            final Map<String, T> map = mapClass.newInstance();
            return map;
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        }
        return new HashMap<String, T>();
    }

    @Nullable
    @Override
    public Map<String, T> getDefault() {
        if (defMap == null) {
            return null;
        }
        Map<String, T> map = getNewTypeMap();
        map.putAll(defMap);
        return map;
    }
}
