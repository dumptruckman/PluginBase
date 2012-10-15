package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;

public interface CommandHandler {

    boolean registerCommand(Class<? extends Command> commandClass);

    boolean locateAndRunCommand(BasePlayer player, String[] args);
}
