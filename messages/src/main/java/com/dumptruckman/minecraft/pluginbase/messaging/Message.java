/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A localization key and its defaults.
 *
 * The key represents the location of the localized strings in a language file.
 * The default is what should populate the localization file by default.
 */
public final class Message {

    private final List<String> def;
    private final String path;

    /**
     * Creates a new localization message.
     *
     * These messages are automatically added to the localization pool which is generally written out to a default
     * language file.  This means that this Message needs to be initialized prior to the default language file being
     * generated.
     *
     * @param key The localization key for this message.
     * @param def The main message line.  In some rare cases, this may be the only message used.
     * @param extra Additional lines for the message.
     */
    public Message(final String key, final String def, final String... extra) {
        this.path = key;
        this.def = new ArrayList<String>(extra.length + 1);
        this.def.add(def);
        this.def.addAll(Arrays.asList(extra));
        Messages.registerMessage(this);
    }

    /**
     * The default messages in whatever your pimary plugin language is.
     *
     * @return The default non-localized messages.
     */
    public List<String> getDefault() {
        return def;
    }

    /**
     * The localization key for the message.
     *
     * @return The localization key for the message.
     */
    public String getPath() {
        return path;
    }
}
