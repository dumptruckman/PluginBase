package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.pneumaticraft.commandhandler.CommandHandler;

public interface BukkitPlugin<C extends BaseConfig> extends PluginBase<C> {
    
    CommandHandler getCommandHandler();

    public Messager getMessager();

    public void setMessager(Messager messager);
}
