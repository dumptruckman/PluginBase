/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;

import java.util.ArrayList;
import java.util.List;

class DefaultListProperty<T> extends DefaultValueProperty<T> implements ListProperty<T> {

    private final Class<? extends List> listClass;
    private final List<T> defList;

    public DefaultListProperty(Class<T> type, String path, List<T> def, List<String> comments, List<String> aliases,
                               PropertySerializer<T> serializer, PropertyValidator validator, Message description,
                               boolean deprecated, boolean defaultIfMissing, Class<? extends List> listClass) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.listClass = listClass;
        this.defList = def;
    }

    public List<T> getDefault() {
        return defList;
    }

    @Override
    public List<T> getNewTypeList() {
        try {
            return (List<T>) listClass.newInstance();
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        }
        return new ArrayList<T>();
    }
}
