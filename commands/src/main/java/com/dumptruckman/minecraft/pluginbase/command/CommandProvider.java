package com.dumptruckman.minecraft.pluginbase.command;

import org.jetbrains.annotations.NotNull;

public interface CommandProvider extends QueuedCommandProvider {

    @NotNull
    String getCommandPrefix();

    @NotNull
    CommandHandler getCommandHandler();
}
