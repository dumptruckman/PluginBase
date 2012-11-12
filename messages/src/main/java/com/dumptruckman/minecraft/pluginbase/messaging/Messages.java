/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class Messages {

    protected static final Map<String, Message> messages = new HashMap<String, Message>();
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    
    // Generic Messages
    public final static Message BLANK = new Message(null, "");

    // Property Messages
    public final static Message PROPERTY_CHANGED_DENIED = new Message("properties.change_denied", "The value '%s' was denied for property '%s'");

    // END CHECKSTYLE-SUPPRESSION: Javadoc

    static void registerMessage(Message message) {
        messages.put(message.getPath(), message);
    }
}

