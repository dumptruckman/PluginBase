/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryBuilder<T> {

    private String path;
    private T def = null;
    private List<T> defList = null;
    private Map<String, T> defMap = null;
    private List<String> comments = new ArrayList<String>();
    private Class<T> type;
    private Message description = null;
    private EntrySerializer<T> serializer;
    private EntryValidator validator;
    private boolean deprecated = false;
    private boolean defaultIfMissing = true;

    public EntryBuilder(Class<T> type, String name) {
        this.path = name;
        this.type = type;
        if (type.equals(String.class)) {
            this.serializer = new StringStringSerializer<T>(type);
        } else {
            try {
                this.serializer = new DefaultStringSerializer<T>(type);
            } catch (IllegalArgumentException e) {
                this.serializer = new DefaultSerializer<T>(type);
            }
        }
        this.validator = new DefaultValidator();
    }
    
    public EntryBuilder<T> def(T def) {
        this.def = def;
        return this;
    }

    public EntryBuilder<T> defList(List<T> def) {
        this.defList = def;
        return this;
    }

    public EntryBuilder<T> defMap(Map<String, T> def) {
        this.defMap = def;
        return this;
    }

    public EntryBuilder<T> comment(String comment) {
        comments.add(comment);
        return this;
    }

    public EntryBuilder<T> serializer(EntrySerializer<T> customSerializer) {
        serializer = customSerializer;
        return this;
    }

    public EntryBuilder<T> validator(EntryValidator validator) {
        this.validator = validator;
        return this;
    }

    public EntryBuilder<T> description(Message message) {
        description = message;
        return this;
    }

    public EntryBuilder<T> deprecated() {
        deprecated = true;
        return this;
    }

    public EntryBuilder<T> allowNull() {
        defaultIfMissing = false;
        return this;
    }

    public SimpleConfigEntry<T> build() {
        return new DefaultSimpleConfigEntry<T>(type, path, def, comments, serializer, validator, description, deprecated, defaultIfMissing);
    }

    public MappedConfigEntry<T> buildMap() {
        return new DefaultMappedConfigEntry<T>(type, path, defMap, comments, serializer, validator, description, deprecated, defaultIfMissing, HashMap.class);
    }

    public MappedConfigEntry<T> buildMap(Class<? extends Map> mapClass) {
        return new DefaultMappedConfigEntry<T>(type, path, defMap, comments, serializer, validator, description, deprecated, defaultIfMissing, mapClass);
    }

    public ListConfigEntry<T> buildList() {
        return new DefaultListConfigEntry<T>(type, path, defList, comments, serializer, validator, description, deprecated, defaultIfMissing, ArrayList.class);
    }

    public ListConfigEntry<T> buildList(Class<? extends List> listClass) {
        return new DefaultListConfigEntry<T>(type, path, defList, comments, serializer, validator, description, deprecated, defaultIfMissing, listClass);
    }
}
