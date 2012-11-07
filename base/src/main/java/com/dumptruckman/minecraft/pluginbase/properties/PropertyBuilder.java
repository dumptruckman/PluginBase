/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.ArrayList;
import java.util.List;

abstract class PropertyBuilder<T> {

    protected final String path;
    protected final Class<T> type;
    protected final List<String> comments = new ArrayList<String>();

    public PropertyBuilder(Class<T> type, String name) {
        this.path = name;
        this.type = type;
    }

    public PropertyBuilder<T> comment(String comment) {
        comments.add(comment);
        return this;
    }

    public abstract Property<T> build();
}
