/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultMappedProperty<T> extends DefaultProperty<T> implements MappedProperty<T> {

    private final Class<? extends Map> mapClass;
    private final Map<String, T> defMap;

    public DefaultMappedProperty(Class<T> type, String path, Map<String, T> def, List<String> comments, List<String> aliases,
                                 PropertySerializer<T> serializer, PropertyValidator validator, Message description,
                                 boolean deprecated, boolean defaultIfMissing, Class<? extends Map> mapClass) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.mapClass = mapClass;
        this.defMap = def;
    }

    @Override
    public Map<String, T> getNewTypeMap() {
        try {
            return (Map<String, T>) mapClass.newInstance();
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        }
        return new HashMap<String, T>();
    }

    @Override
    public Map<String, T> getDefault() {
        return defMap;
    }
}
