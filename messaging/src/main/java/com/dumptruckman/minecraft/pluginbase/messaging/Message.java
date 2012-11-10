/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

    private final List<String> def;
    private final String path;

    public Message(String path, String def, String... extra) {
        this.path = path;
        this.def = new ArrayList<String>(extra.length + 1);
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
