/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

class DefaultProperty<T> implements Property<T> {

    @NotNull
    private final String path;
    @NotNull
    private final List<String> comments;
    @NotNull
    private final Class<T> type;

    DefaultProperty(@NotNull final Class<T> type,
                    @NotNull final String path,
                    @NotNull final List<String> comments) {
        this.type = type;
        this.path = path;
        this.comments = Collections.unmodifiableList(comments);
    }

    @NotNull
    @Override
    public final String getName() {
        return path;
    }

    @NotNull
    @Override
    public final Class<T> getType() {
        return type;
    }

    @NotNull
    @Override
    public final List<String> getComments() {
        return comments;
    }
}
