package com.dumptruckman.minecraft.pluginbase.command.builtin;

import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract class BaseBuiltInCommand extends BuiltInCommand<PluginBase> {

    protected BaseBuiltInCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    @NotNull
    public abstract List<String> getStaticAliases();

    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
