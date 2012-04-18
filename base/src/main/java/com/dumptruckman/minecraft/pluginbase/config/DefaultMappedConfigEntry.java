package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultMappedConfigEntry<T> extends DefaultConfigEntry<T> implements MappedConfigEntry<T> {

    private String additionalPath = "";
    private Class<? extends Map> mapClass;

    public DefaultMappedConfigEntry(Class<T> type, String path, T def, List<String> comments,
                                    EntrySerializer<T> serializer, EntryValidator validator, Message description,
                                    Class<? extends Map> mapClass) {
        super(type, path, def, comments, serializer, validator, description);
        this.mapClass = mapClass;
    }

    public String getName() {
        String result = super.getName() + this.additionalPath;
        this.additionalPath = "";
        return result;
    }

    @Override
    public Map<String, T> getNewTypeMap() {
        try {
            return (Map<String, T>) mapClass.newInstance();
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        }
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
