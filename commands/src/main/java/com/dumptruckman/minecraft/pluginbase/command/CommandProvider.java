package com.dumptruckman.minecraft.pluginbase.command;

public interface CommandProvider {

    String getCommandPrefix();

    CommandHandler getCommandHandler();
}
