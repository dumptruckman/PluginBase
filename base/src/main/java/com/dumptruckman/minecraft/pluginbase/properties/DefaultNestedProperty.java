/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;

class DefaultNestedProperty<T extends NestedProperties> extends DefaultProperty<T> implements NestedProperty<T> {

    DefaultNestedProperty(Class<T> type, String path, List<String> comments) {
        super(type, path, comments);
    }
}
