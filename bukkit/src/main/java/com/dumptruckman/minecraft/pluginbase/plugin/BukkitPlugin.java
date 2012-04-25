package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.util.commandhandler.CommandHandler;
import org.bukkit.plugin.Plugin;

public interface BukkitPlugin<C extends BaseConfig> extends PluginBase<C>, Plugin {
    
    CommandHandler getCommandHandler();

    public Messager getMessager();

    public void setMessager(Messager messager);
}
