package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultMappedConfigEntry<T> extends DefaultConfigEntry<T> implements MappedConfigEntry<T> {

    private Class<? extends Map> mapClass;

    public DefaultMappedConfigEntry(Class<T> type, String path, T def, List<String> comments,
                                    EntrySerializer<T> serializer, EntryValidator validator, Message description,
                                    boolean deprecated, boolean defaultIfMissing, Class<? extends Map> mapClass) {
        super(type, path, def, comments, serializer, validator, description, deprecated, defaultIfMissing);
        this.mapClass = mapClass;
    }

    @Override
    public Map<String, T> getNewTypeMap() {
        try {
            return (Map<String, T>) mapClass.newInstance();
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to HashMap");
        }
        return new HashMap<String, T>();
    }
}
