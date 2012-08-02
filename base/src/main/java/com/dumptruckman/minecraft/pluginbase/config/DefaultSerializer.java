/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

class DefaultSerializer<T> implements EntrySerializer<T> {

    private Class<T> type;

    public DefaultSerializer(Class<T> type) {
        this.type = type;
    }

    private Class<T> getType() {
        return this.type;
    }

    @Override
    public T deserialize(Object obj) {
        return getType().cast(obj);
    }

    @Override
    public Object serialize(T t) {
        return t;
    }
}
