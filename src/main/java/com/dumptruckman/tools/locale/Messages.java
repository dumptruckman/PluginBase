package com.dumptruckman.tools.locale;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class Messages {
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    
    // Generic Messages
    public final static Message GENERIC_ERROR = new Message("GENERIC_ERROR", "[Error]");
    public final static Message GENERIC_SUCCESS = new Message("GENERIC_SUCCESS", "[Success]");
    public final static Message GENERIC_INFO = new Message("GENERIC_INFO", "[Info]");
    public final static Message GENERIC_HELP = new Message("GENERIC_HELP", "[Help]");
    public final static Message GENERIC_OFF = new Message("GENERIC_OFF", "OFF");

    // Reload Command
    public final static Message RELOAD_COMPLETE = new Message("RELOAD_COMPLETE", "&b===[ Reload Complete! ]===");

    // DebugCommand
    public final static Message INVALID_DEBUG = new Message("INVALID_DEBUG",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");
    public final static Message DEBUG_SET = new Message("DEBUG_SET", "Debug mode is %1");

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    public static final Map<String, Message> messages = new HashMap<String, Message>();

    public static void registerMessage(Message message) {
        messages.put(message.getPath(), message);
        System.out.println(messages.get(message.getPath()));
    }
    
    public static Map<String, Message> getMessages() {
        return messages;
    }
}

