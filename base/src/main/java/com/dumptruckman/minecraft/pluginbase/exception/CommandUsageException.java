package com.dumptruckman.minecraft.pluginbase.exception;

import com.sk89q.minecraft.util.commands.CommandException;

import java.util.List;

public class CommandUsageException extends CommandException {

    protected List<String> usage;

    public CommandUsageException(String message, List<String> usage) {
        super(message);
        this.usage = usage;
    }

    public List<String> getUsage() {
        return usage;
    }
}
