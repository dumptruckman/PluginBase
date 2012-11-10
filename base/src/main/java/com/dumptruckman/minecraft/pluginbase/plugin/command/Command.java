package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;

public abstract class Command<P extends PluginBase> {

    public abstract Perm getPerm();

    public abstract Message getHelp();

    public abstract boolean runCommand(P p, BasePlayer sender, CommandContext context);
}
