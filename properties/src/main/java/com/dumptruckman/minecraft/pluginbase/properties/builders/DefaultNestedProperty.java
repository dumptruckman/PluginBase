/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class DefaultNestedProperty<T extends NestedProperties> extends DefaultProperty<T> implements NestedProperty<T> {

    DefaultNestedProperty(@NotNull final Class<T> type, @NotNull final String path,
                          @NotNull final List<String> comments) {
        super(type, path, comments);
    }
}
