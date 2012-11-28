package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;

public class CommandPerms {

    /**
     * Permission for confirm command.
     */
    public static final Perm COMMAND_CONFIRM = PermFactory.newPerm("cmd.confirm").usePluginName().commandPermission()
            .desc("If you have not been prompted to use this, it will not do anything.").build();
    /**
     * Permission for debug command.
     */
    public static final Perm COMMAND_DEBUG = PermFactory.newPerm("cmd.debug").usePluginName().commandPermission()
            .desc("Spams the console a bunch.").build();
    /**
     * Permission for info command.
     */
    public static final Perm COMMAND_INFO = PermFactory.newPerm("cmd.info").usePluginName().commandPermission()
            .desc("Gives some basic information about this plugin.").build();
    /**
     * Permission for reload command.
     */
    public static final Perm COMMAND_RELOAD = PermFactory.newPerm("cmd.reload").usePluginName().commandPermission()
            .desc("Reloads the config file.").build();
    /**
     * Permission for version command.
     */
    public static final Perm COMMAND_VERSION = PermFactory.newPerm("cmd.version").usePluginName().commandPermission()
            .desc("Sends version information to the console.").build();
    /**
     * Permission for help command.
     */
    public static final Perm COMMAND_HELP = PermFactory.newPerm("cmd.help").usePluginName().commandPermission()
            .desc("Displays a nice help menu.").build();
}
