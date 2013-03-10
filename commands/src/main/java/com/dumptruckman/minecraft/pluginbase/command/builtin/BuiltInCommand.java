package com.dumptruckman.minecraft.pluginbase.command.builtin;

import com.dumptruckman.minecraft.pluginbase.command.Command;
import com.dumptruckman.minecraft.pluginbase.command.CommandContext;
import com.dumptruckman.minecraft.pluginbase.command.CommandProvider;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.Messaging;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BuiltInCommand<P extends CommandProvider & Messaging> extends Command<P> {

    protected BuiltInCommand(@NotNull final P plugin) {
        super(plugin);
    }

    @NotNull
    public abstract List<String> getStaticAliases();

    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
