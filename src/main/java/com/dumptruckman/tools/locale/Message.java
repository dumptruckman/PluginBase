package com.dumptruckman.tools.locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

    private final List<String> def;
    private final String path;

    public Message(String path, String def, String... extra) {
        this.path = path;
        this.def = new ArrayList<String>();
        this.def.add(def);
        this.def.addAll(Arrays.asList(extra));
        Messages.registerMessage(this);
    }

    /**
     * @return This {@link Messages}'s default-message
     */
    public List<String> getDefault() {
        return def;
    }
    
    public String getPath() {
        return path;
    }
}
