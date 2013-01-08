package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.messaging.Messager;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

public abstract class Command<P extends PluginBase> {

    private P plugin;

    protected Command(@NotNull final P plugin) {
        this.plugin = plugin;
    }

    protected P getPlugin() {
        return plugin;
    }

    protected Messager getMessager() {
        return getPlugin().getMessager();
    }

    public abstract Perm getPerm();

    public abstract Message getHelp();

    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    final String getClassName() {
        return getClass().getName();
    }
}
