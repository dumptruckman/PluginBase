package com.dumptruckman.minecraft.plugin;

import com.dumptruckman.minecraft.config.BaseConfig;
import com.dumptruckman.minecraft.locale.Messager;
import com.dumptruckman.minecraft.locale.SimpleMessager;
import com.pneumaticraft.commandhandler.CommandHandler;

public interface BukkitPlugin<C extends BaseConfig> extends PluginBase<C> {
    
    CommandHandler getCommandHandler();

    public Messager getMessager();

    public void setMessager(Messager messager);
}
