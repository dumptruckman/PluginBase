package com.dumptruckman.minecraft.pluginbase.command;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.SendablePluginBaseException;
import org.jetbrains.annotations.NotNull;

public class CommandException extends SendablePluginBaseException {

    public CommandException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    public CommandException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    public CommandException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause) {
        super(languageMessage, cause);
    }

    public CommandException(@NotNull final PluginBaseException e) {
        super(e);
    }
}
