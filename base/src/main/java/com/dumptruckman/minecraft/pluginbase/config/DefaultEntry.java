/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;

abstract class DefaultEntry<T> implements Entry<T> {

    private final String path;
    private final List<String> comments;
    private final Class<T> type;
    private final EntrySerializer<T> serializer;
    private final EntryValidator validator;
    private final Message description;
    private final boolean deprecated;
    private final boolean defaultIfMissing;

    public DefaultEntry(Class<T> type, String path, List<String> comments,
                        EntrySerializer<T> serializer, EntryValidator validator, Message description,
                        boolean deprecated, boolean defaultIfMissing) {
        this.path = path;
        this.comments = comments;
        this.type = type;
        this.serializer = serializer;
        this.validator = validator;
        this.description = description;
        this.deprecated = deprecated;
        this.defaultIfMissing = defaultIfMissing;
    }

    public String getName() {
        return this.path;
    }

    public Class<T> getType() {
        return this.type;
    }

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    public List<String> getComments() {
        return this.comments;
    }

    public boolean isValid(Object obj) {
        return validator.isValid(obj);
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public boolean shouldDefaultIfMissing() {
        return defaultIfMissing;
    }

    public Message getInvalidMessage() {
        return validator.getInvalidMessage();
    }

    @Override
    public Message getDescription() {
        return description;
    }

    @Override
    public Object serialize(T value) {
        return serializer.serialize(value);
    }

    @Override
    public T deserialize(Object o) {
        return serializer.deserialize(o);
    }


}
