/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class Messages {

    protected static final Map<String, Message> messages = new HashMap<String, Message>();
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    
    // Generic Messages
    public final static Message GENERIC_OFF = new Message("generic.off", "OFF");
    public final static Message BLANK = new Message(null, "");

    public final static Message INVALID_DEBUG = new Message("debug.invalid",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");

    // END CHECKSTYLE-SUPPRESSION: Javadoc

    public static void registerMessage(Message message) {
        messages.put(message.getPath(), message);
    }
}

