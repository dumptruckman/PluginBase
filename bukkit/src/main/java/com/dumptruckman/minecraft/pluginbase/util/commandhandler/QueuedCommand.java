package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import com.dumptruckman.minecraft.pluginbase.plugin.command.QueuedPluginCommand;

import java.util.List;

class QueuedCommand {

    private QueuedPluginCommand command;
    private List<String> args;
    private long expirationTime;

    QueuedCommand(QueuedPluginCommand command, List<String> args, long expirationTime) {
        this.command = command;
        this.args = args;
        this.expirationTime = expirationTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public QueuedPluginCommand getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }
}
