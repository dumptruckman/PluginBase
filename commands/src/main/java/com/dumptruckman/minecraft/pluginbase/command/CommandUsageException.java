package com.dumptruckman.minecraft.pluginbase.command;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandUsageException extends CommandException {

    private final List<String> usage;

    public CommandUsageException(@NotNull final BundledMessage languageMessage, final List<String> usage) {
        super(languageMessage);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable, final List<String> usage) {
        super(languageMessage, throwable);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause, final List<String> usage) {
        super(languageMessage, cause);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final PluginBaseException e, final List<String> usage) {
        super(e);
        this.usage = usage;
    }

    public List<String> getUsage() {
        return usage;
    }
}
