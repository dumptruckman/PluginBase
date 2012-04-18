package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultListConfigEntry<T> extends DefaultConfigEntry<T> implements ListConfigEntry<T> {

    private String additionalPath = "";
    private Class<? extends List> listClass;

    public DefaultListConfigEntry(Class<T> type, String path, T def, List<String> comments,
                                  EntrySerializer<T> serializer, EntryValidator validator, Message description,
                                  Class<? extends List> listClass) {
        super(type, path, def, comments, serializer, validator, description);
        this.listClass = listClass;
    }

    @Override
    public List<T> getNewTypeList() {
        try {
            return (List<T>) listClass.newInstance();
        } catch (InstantiationException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        } catch (IllegalAccessException e) {
            Logging.warning("Could not instantiate desired class, defaulting to ArrayList");
        }
        return new ArrayList<T>();
    }
}
