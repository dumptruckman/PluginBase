package pluginbase.bukkit.command;

import org.bukkit.plugin.Plugin;
import pluginbase.bukkit.minecraft.BukkitTools;
import pluginbase.command.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandProvider;
import pluginbase.logging.LoggablePlugin;
import pluginbase.messages.messaging.Messaging;

public class BukkitCommandHandler<P extends CommandProvider & Messaging & LoggablePlugin> extends CommandHandler<P> {

    private final Plugin executor;
    private CommandMap fallbackCommands;

    public BukkitCommandHandler(P plugin, Plugin executor) {
        super(plugin);
        this.executor = executor;
    }

    protected boolean register(@NotNull final CommandRegistration<P> commandInfo, @NotNull final pluginbase.command.Command<P> command) {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null) {
            return false;
        }
        String[] aliases = commandInfo.getAliases();
        /*
        String[] bukkitCompatibleAliases = new String[aliases.length];
        for (int i = 0; i < aliases.length; i++) {
            if (i == 0) {
                bukkitCompatibleAliases[i] = aliases[i];
            } else {
                bukkitCompatibleAliases[i] = PATTERN_ON_SPACE.split(aliases[i])[0];
            }
        }
        */
        DynamicPluginCommand<P> cmd = new DynamicPluginCommand<P>(aliases, commandInfo.getDesc(),
                "/" + commandInfo.getName() + " " + commandInfo.getUsage(), executor, commandInfo.getRegisteredWith(), executor);
        CommandHelpTopic helpTopic = new CommandHelpTopic(cmd, command.getHelp());
        cmd.setPermissions(commandInfo.getPermissions());
        if (commandMap.register(commandInfo.getName(), plugin.getName(), cmd)) {
            Bukkit.getServer().getHelpMap().addTopic(helpTopic);
            return true;
        }
        return false;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = ReflectionUtil.getField(executor.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            if (fallbackCommands != null) {
                commandMap = fallbackCommands;
            } else {
                getLog().warning("Could not retrieve server CommandMap, using fallback instead!");
                fallbackCommands = commandMap = new SimpleCommandMap(Bukkit.getServer());
                Bukkit.getServer().getPluginManager().registerEvents(new FallbackRegistrationListener(fallbackCommands), executor);
            }
        }
        return commandMap;
    }

    boolean hasPermission(final CommandSender sender, final String permission) {
        return BukkitTools.wrapSender(sender).hasPermission(permission);
    }
}
