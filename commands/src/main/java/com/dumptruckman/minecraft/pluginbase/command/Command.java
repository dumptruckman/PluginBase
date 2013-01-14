package com.dumptruckman.minecraft.pluginbase.command;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.messaging.Messager;
import com.dumptruckman.minecraft.pluginbase.messaging.Messaging;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.sk89q.minecraft.util.commands.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command<P extends CommandProvider & Messaging> {

    private P plugin;

    protected Command(@NotNull final P plugin) {
        this.plugin = plugin;
    }

    @NotNull
    protected P getPlugin() {
        return plugin;
    }

    @NotNull
    protected Messager getMessager() {
        return getPlugin().getMessager();
    }

    @Nullable
    public abstract Perm getPerm();

    @Nullable
    public abstract Message getHelp();

    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    @NotNull
    final String getClassName() {
        return getClass().getName();
    }
}
