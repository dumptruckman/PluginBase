/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.ArrayList;
import java.util.List;

class DefaultListConfigEntry<T> extends DefaultConfigEntry<T> implements ListConfigEntry<T> {

    private final Class<? extends List> listClass;
    private final List<T> defList;

    public DefaultListConfigEntry(Class<T> type, String path, List<T> def, List<String> comments,
                                  EntrySerializer<T> serializer, EntryValidator validator, Message description,
                                  boolean deprecated, boolean defaultIfMissing, Class<? extends List> listClass) {
        super(type, path, comments, serializer, validator, description, deprecated, defaultIfMissing);
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
