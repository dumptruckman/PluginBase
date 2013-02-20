/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.properties.Property;

import java.util.Collections;
import java.util.List;

class DefaultProperty<T> implements Property<T> {

    private final String path;
    private final List<String> comments;
    private final Class<T> type;

    public DefaultProperty(Class<T> type, String path, List<String> comments) {
        this.type = type;
        this.path = path;
        this.comments = Collections.unmodifiableList(comments);
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public List<String> getComments() {
        return comments;
    }
}
