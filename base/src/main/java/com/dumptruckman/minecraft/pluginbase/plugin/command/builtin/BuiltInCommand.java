package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.Command;
import com.sk89q.minecraft.util.commands.CommandContext;

import java.util.List;

public abstract class BuiltInCommand extends Command<PluginBase<BaseConfig>> {

    public abstract List<String> getStaticAliases();

    public abstract void runCommand(final PluginBase<BaseConfig> p, final BasePlayer sender, final CommandContext context);
}
