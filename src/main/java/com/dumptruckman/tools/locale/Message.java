package com.dumptruckman.tools.locale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

    private final List<String> def;

    Message(String def, String... extra) {
        this.def = new ArrayList<String>();
        this.def.add(def);
        this.def.addAll(Arrays.asList(extra));
    }

    /**
     * @return This {@link BaseMessages}'s default-message
     */
    public List<String> getDefault() {
        return def;
    }
}
