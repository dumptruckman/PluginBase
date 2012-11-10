/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

class DefaultNullProperty extends DefaultValueProperty<Null> implements NullProperty {

    public DefaultNullProperty(String path, List<String> comments, List<String> aliases,
                               PropertySerializer<Null> serializer, PropertyValidator validator, Message description,
                               boolean deprecated, boolean defaultIfMissing) {
        super(Null.class, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
    }

    @Override
    public Object getDefault() {
        return null;
    }
}
