package com.dumptruckman.tools.locale;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing all messages/strings used by PluginBase.
 */
public class Messages {

    protected static final Map<String, Message> messages = new HashMap<String, Message>();
    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc
    
    // Generic Messages
    public final static Message GENERIC_ERROR = new Message("generic.error", "[Error]");
    public final static Message GENERIC_SUCCESS = new Message("generic.success", "[Success]");
    public final static Message GENERIC_INFO = new Message("generic.info", "[Info]");
    public final static Message GENERIC_HELP = new Message("generic.help", "[Help]");
    public final static Message GENERIC_OFF = new Message("generic.off", "OFF");

    // Reload Command
    public final static Message RELOAD_COMPLETE = new Message("cmd.reload.complete", "&b===[ Reload Complete! ]===");

    // DebugCommand
    public final static Message INVALID_DEBUG = new Message("cmd.debug.invalid",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is %1");

    // END CHECKSTYLE-SUPPRESSION: Javadoc

    public static void registerMessage(Message message) {
        messages.put(message.getPath(), message);
    }
}

