package com.dumptruckman.minecraft.locale;

public class CommandMessages {

    public static void init() { }

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    // Reload Command
    public final static Message RELOAD_NAME = new Message("cmd.reload.name", "Reloads config file");
    public final static Message RELOAD_COMPLETE = new Message("cmd.reload.complete", "&b===[ Reload Complete! ]===");

    // DebugCommand
    public final static Message DEBUG_NAME = new Message("cmd.debug.name", "Turn Debug on/off?");
    public final static Message DEBUG_USAGE = new Message("cmd.debug.usage", "/%1 debug &6[1|2|3|off]");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is %1");

    // HelpCommand
    public final static Message HELP_NAME = new Message("cmd.help.name", "Get help with %1");
    public final static Message HELP_USAGE = new Message("cmd.help.usage", "/%1 &6[FILTER] [PAGE #]");
    public final static Message HELP_TITLE = new Message("cmd.help.title", "&b====[ %1 Help ]====");
    public final static Message HELP_NONE_FOUND = new Message("cmd.help.none_found", "&cSorry... &fNo commands matched your filter: &b%1");
    public final static Message HELP_MORE_INFO = new Message("cmd.help.more_info", "&b Add a '&5?&b' after a command to see more about it.");
    public final static Message HELP_PAGES = new Message("cmd.help.pages", "&b Page %1 of %2");

    // END CHECKSTYLE-SUPPRESSION: Javadoc
}
