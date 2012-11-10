/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

public class NestedPropertyBuilder<T extends NestedProperties> extends PropertyBuilder<T> {

    public NestedPropertyBuilder(Class<T> type, String name) {
        super(type, name);
    }

    public NestedPropertyBuilder<T> comment(String comment) {
        return (NestedPropertyBuilder<T>) super.comment(comment);
    }

    public NestedProperty<T> build() {
        return new DefaultNestedProperty<T>(type, path, comments);
    }
}
