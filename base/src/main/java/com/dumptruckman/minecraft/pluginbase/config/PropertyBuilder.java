/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyBuilder<T> {

    private String path;
    private T def = null;
    private List<T> defList = null;
    private Map<String, T> defMap = null;
    private List<String> comments = new ArrayList<String>();
    private Set<String> aliases = new LinkedHashSet<String>();
    private Class<T> type;
    private Message description = null;
    private PropertySerializer<T> serializer;
    private PropertyValidator validator;
    private boolean deprecated = false;
    private boolean defaultIfMissing = true;

    public PropertyBuilder(Class<T> type, String name) {
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
    
    public PropertyBuilder<T> def(T def) {
        this.def = def;
        return this;
    }

    public PropertyBuilder<T> defList(List<T> def) {
        this.defList = def;
        return this;
    }

    public PropertyBuilder<T> defMap(Map<String, T> def) {
        this.defMap = def;
        return this;
    }

    public PropertyBuilder<T> comment(String comment) {
        comments.add(comment);
        return this;
    }

    public PropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        serializer = customSerializer;
        return this;
    }

    public PropertyBuilder<T> validator(PropertyValidator validator) {
        this.validator = validator;
        return this;
    }

    public PropertyBuilder<T> description(Message message) {
        description = message;
        return this;
    }

    public PropertyBuilder<T> deprecated() {
        deprecated = true;
        return this;
    }

    public PropertyBuilder<T> allowNull() {
        defaultIfMissing = false;
        return this;
    }

    public PropertyBuilder<T> alias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public SimpleProperty<T> build() {
        return new DefaultSimpleProperty<T>(type, path, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing);
    }

    public MappedProperty<T> buildMap() {
        return new DefaultMappedProperty<T>(type, path, defMap, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, HashMap.class);
    }

    public MappedProperty<T> buildMap(Class<? extends Map> mapClass) {
        return new DefaultMappedProperty<T>(type, path, defMap, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, mapClass);
    }

    public ListProperty<T> buildList() {
        return new DefaultListProperty<T>(type, path, defList, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, ArrayList.class);
    }

    public ListProperty<T> buildList(Class<? extends List> listClass) {
        return new DefaultListProperty<T>(type, path, defList, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, listClass);
    }
}
