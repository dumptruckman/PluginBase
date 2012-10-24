package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.sk89q.bukkit.util.FallbackRegistrationListener;
import com.sk89q.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

public class BukkitCommandHandler extends CommandHandler<BukkitPlugin> {

    private final CommandExecutor executor;
    private CommandMap fallbackCommands;

    public BukkitCommandHandler(BukkitPlugin plugin) {
        this(plugin, plugin);
    }

    public BukkitCommandHandler(BukkitPlugin plugin, CommandExecutor executor) {
        super(plugin);
        this.executor = executor;
    }

    protected boolean register(com.sk89q.bukkit.util.CommandInfo command) {
        CommandMap commandMap = getCommandMap();
        if (command == null || commandMap == null) {
            return false;
        }
        DynamicPluginCommand cmd = new DynamicPluginCommand(command.getAliases(), command.getDesc(),
                "/" + command.getName() + " " + command.getUsage(), executor, command.getRegisteredWith(), plugin);
        cmd.setPermissions(command.getPermissions());
        commandMap.register(command.getName(), plugin.getDescription().getName(), cmd);
        return true;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = ReflectionUtil.getField(plugin.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            if (fallbackCommands != null) {
                commandMap = fallbackCommands;
            } else {
                Logging.warning("Could not retrieve server CommandMap, using fallback instead!");
                fallbackCommands = commandMap = new SimpleCommandMap(Bukkit.getServer());
                Bukkit.getServer().getPluginManager().registerEvents(new FallbackRegistrationListener(fallbackCommands), plugin);
            }
        }
        return commandMap;
    }

    boolean hasPermission(final CommandSender sender, final String permission) {
        return plugin.wrapSender(sender).hasPermission(permission);
    }
}
