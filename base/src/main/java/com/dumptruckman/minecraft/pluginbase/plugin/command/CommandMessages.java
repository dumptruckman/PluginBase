/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;

public class CommandMessages {

    public static void init() { }

    // BEGIN CHECKSTYLE-SUPPRESSION: Javadoc

    public final static Message GENERIC_OFF = new Message("generic.off", "OFF");

    // Reload Command
    public final static Message RELOAD_HELP = new Message("cmd.reload.help", "Reloads the plugin, typically detecting any external changes in plugin files.");
    public final static Message RELOAD_COMPLETE = new Message("cmd.reload.complete", "&b===[ Reload Complete! ]===");

    // ConfirmCommand
    public final static Message CONFIRM_NAME = new Message("cmd.confirm.name", "Confirms a command that could destroy life, the universe and everything.");
    public final static Message CONFIRM_MESSAGE = new Message("cmd.confirm.confirm_message",
            "The command &c%s&f has been halted due to the fact that it could break something!",
            "If you still wish to execute &c%s&f");
    public final static Message CONFIRM_MESSAGE_2 = new Message("cmd.confirm.confirm_message_2",
            "please type: &a/%s",
            "&a/%s&f will only be available for %s seconds.");
    public final static Message QUEUED_EXPIRED = new Message("cmd.queued.expired",
            "This command has expired. Please type the original command again.");
    public final static Message QUEUED_NONE = new Message("cmd.queued.none",
            "You have no commands queued.  Try the original command again.");

    // DebugCommand
    public final static Message DEBUG_HELP = new Message("cmd.debug.help", "Enables or disable debug mode."
            + "When enabled the console will be spammed with lots of information useful for helping developers debug.");
    public final static Message DEBUG_SET = new Message("cmd.debug.set", "Debug mode is &2%s");
    public final static Message DEBUG_DISABLED = new Message("cmd.debug.off", "Debug mode is &cOFF");
    public final static Message INVALID_DEBUG = new Message("debug.invalid",
            "&fInvalid debug level.  Please use number 0-3.  &b(3 being many many messages!)");

    // InfoCommand
    public final static Message INFO_HELP = new Message("cmd.info.help", "Gives some basic information about this plugin.");

    // ReloadCommand
    public final static Message VERSION_HELP = new Message("cmd.version.help",
            "Displays version and other helpful information about the plugin.",
            "Flags:",
            "  -p will output an http://pastie.org url containing the information.",
            "  -b will output an http://pastebin.com url containing the information.");
    public final static Message VERSION_PLAYER = new Message("cmd.version.player",
            "Version info dumped to console. Please check your server logs.");
    public final static Message VERSION_PLUGIN_VERSION = new Message("cmd.version.info.plugin_version", "%s Version: %s");
    public final static Message VERSION_SERVER_NAME = new Message("cmd.version.info.server_name", "Server Name: %s");
    public final static Message VERSION_SERVER_VERSION = new Message("cmd.version.info.server_version", "Server Version: %s");
    public final static Message VERSION_LANG_FILE = new Message("cmd.version.info.lang_file", "Language file: %s");
    public final static Message VERSION_DEBUG_MODE = new Message("cmd.version.info.debug_mode", "Debug Mode: %s");
    public final static Message VERSION_INFO_DUMPED = new Message("cmd.version.dumped", "Version info dumped here: %s");


    // HelpCommand
    public final static Message HELP_NAME = new Message("cmd.help.name", "Get help with %s");
    public final static Message HELP_USAGE = new Message("cmd.help.usage", "/%s &6[FILTER] [PAGE #]");
    public final static Message HELP_TITLE = new Message("cmd.help.title", "&b====[ %s Help ]====");
    public final static Message HELP_NONE_FOUND = new Message("cmd.help.none_found", "&cSorry... &fNo commands matched your filter: &b%s");
    public final static Message HELP_MORE_INFO = new Message("cmd.help.more_info", "&b Add a '&5?&b' after a command to see more about it.");
    public final static Message HELP_PAGES = new Message("cmd.help.pages", "&b Page %s of %s");

    // END CHECKSTYLE-SUPPRESSION: Javadoc
}
