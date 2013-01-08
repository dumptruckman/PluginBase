package com.dumptruckman.minecraft.pluginbase.plugin.command.builtin;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BuiltInCommand extends Command<PluginBase> {

    protected BuiltInCommand(@NotNull final PluginBase plugin) {
        super(plugin);
    }

    public abstract List<String> getStaticAliases();

    public abstract boolean runCommand(final BasePlayer sender, final CommandContext context);
}
