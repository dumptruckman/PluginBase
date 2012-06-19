package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class PaginatedPluginCommand<T, P extends AbstractBukkitPlugin> extends PaginatedCommand<T, P> {

    /**
     * The reference to the core.
     */
    protected P plugin;

    public PaginatedPluginCommand(P plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public final void addPrefixedKey(String key) {
        for (String prefix : (List<String>) plugin.getCommandPrefixes()) {
            this.addKey(prefix + key);
        }
    }

    protected Messager getMessager() {
        return plugin.getMessager();
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);
}
