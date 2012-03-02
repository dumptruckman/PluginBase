package com.dumptruckman.minecraft.locale;

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
    public final static Message BLANK = new Message(null, "");

    // Reload Command
    public final static Message RELOAD_COMPLETE = new Message("cmd.reload.complete", "&b===[ Reload Complete! ]===");

    // DebugCommand
    public final static Message INVALID_DEBUG = new Message("cmd.debug.invalid",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is %1");
    
    // HelpCommand
    public final static Message HELP_NAME = new Message("cmd.help.name", "Get help with %1");
    public final static Message HELP_USAGE = new Message("cmd.help.usage", "/%1 &6[FILTER] [PAGE #]");
    public final static Message HELP_TITLE = new Message("cmd.help.title", "&b====[ %1 Help ]====");
    public final static Message HELP_NONE_FOUND = new Message("cmd.help.none_found", "&cSorry... &fNo commands matched your filter: &b%1");
    public final static Message HELP_MORE_INFO = new Message("cmd.help.more_info", "&b Add a '&5?&b' after a command to see more about it.");
    public final static Message HELP_PAGES = new Message("cmd.help.pages", "&b Page %1 of %2");


    // END CHECKSTYLE-SUPPRESSION: Javadoc

    public static void registerMessage(Message message) {
        messages.put(message.getPath(), message);
    }
}

