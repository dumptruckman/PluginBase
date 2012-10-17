/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

public class CommandMessages {

    public static void init() { }

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    // Reload Command
    public final static Message RELOAD_HELP = new Message("cmd.reload.help", "Reloads the plugin, typically detecting any external changes in plugin files.");
    public final static Message RELOAD_COMPLETE = new Message("cmd.reload.complete", "&b===[ Reload Complete! ]===");

    // ConfirmCommand
    public final static Message CONFIRM_NAME = new Message("cmd.confirm.name", "Confirms a command that could destroy life, the universe and everything.");
    public final static Message CONFIRM_MESSAGE = new Message("cmd.confirm.confirm_message",
            "The command &c%1&f has been halted due to the fact that it could break something!",
            "If you still wish to execute &c%1&f");
    public final static Message CONFIRM_MESSAGE_2 = new Message("cmd.confirm.confirm_message_2",
            "please type: &a/%1",
            "&a/%1&f will only be available for %2 seconds.");
    public final static Message QUEUED_EXPIRED = new Message("cmd.queued.expired",
            "This command has expired. Please type the original command again.");
    public final static Message QUEUED_NONE = new Message("cmd.queued.none",
            "You have no commands queued.  Try the original command again.");

    // DebugCommand
    public final static Message DEBUG_HELP = new Message("cmd.debug.help", "Enables or disable debug mode."
            + "When enabled the console will be spammed with lots of information useful for helping developers debug.");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is &2%1");
    public final static Message DEBUG_DISABLED = new Message("cmd.debug.off", "Debug mode is &cOFF");

    // ReloadCommand
    public final static Message VERSION_HELP = new Message("cmd.version.help",
            "Displays version and other helpful information about the plugin.",
            "Flags:",
            "  -p will output an http://pastie.org url containing the information.",
            "  -b will output an http://pastebin.com url containing the information.");
    public final static Message VERSION_PLAYER = new Message("cmd.version.player",
            "Version info dumped to console. Please check your server logs.");
    public final static Message VERSION_PLUGIN_VERSION = new Message("cmd.version.info.plugin_version", "%1 Version: %2");
    public final static Message VERSION_SERVER_NAME = new Message("cmd.version.info.server_name", "Server Name: %1");
    public final static Message VERSION_SERVER_VERSION = new Message("cmd.version.info.server_version", "Server Version: %1");
    public final static Message VERSION_LANG_FILE = new Message("cmd.version.info.lang_file", "Language file: %1");
    public final static Message VERSION_DEBUG_MODE = new Message("cmd.version.info.debug_mode", "Debug Mode: %1");
    public final static Message VERSION_INFO_DUMPED = new Message("cmd.version.dumped", "Version info dumped here: %1");


    // HelpCommand
    public final static Message HELP_NAME = new Message("cmd.help.name", "Get help with %1");
    public final static Message HELP_USAGE = new Message("cmd.help.usage", "/%1 &6[FILTER] [PAGE #]");
    public final static Message HELP_TITLE = new Message("cmd.help.title", "&b====[ %1 Help ]====");
    public final static Message HELP_NONE_FOUND = new Message("cmd.help.none_found", "&cSorry... &fNo commands matched your filter: &b%1");
    public final static Message HELP_MORE_INFO = new Message("cmd.help.more_info", "&b Add a '&5?&b' after a command to see more about it.");
    public final static Message HELP_PAGES = new Message("cmd.help.pages", "&b Page %1 of %2");

    // END CHECKSTYLE-SUPPRESSION: Javadoc
}
