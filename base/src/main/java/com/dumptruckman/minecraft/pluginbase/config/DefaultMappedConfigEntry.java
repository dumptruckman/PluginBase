package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultMappedConfigEntry<T> extends DefaultConfigEntry<T> implements MappedConfigEntry<T> {

    private String additionalPath = "";

    public DefaultMappedConfigEntry(Class<T> type, String path, T def, List<String> comments,
                                    EntrySerializer<T> serializer, EntryValidator validator, Message description) {
        super(type, path, def, comments, serializer, validator, description);
    }

    public String getName() {
        String result = super.getName() + this.additionalPath;
        this.additionalPath = "";
        return result;
    }

    @Override
    public Map<String, T> getNewTypeMap() {
        return new HashMap<String, T>();
    }

    @Override
    public MappedConfigEntry<T> specific(String additionalPath) {
        this.additionalPath = "." + additionalPath;
        return this;
    }

    @Override
    public String getSpecificPath() {
        return this.additionalPath;
    }
}
