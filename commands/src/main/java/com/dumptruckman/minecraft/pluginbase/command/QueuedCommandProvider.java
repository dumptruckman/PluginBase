package com.dumptruckman.minecraft.pluginbase.command;

import org.jetbrains.annotations.NotNull;

interface QueuedCommandProvider {

    void scheduleQueuedCommandExpiration(@NotNull QueuedCommand queuedCommand);

    boolean useQueuedCommands();
}
