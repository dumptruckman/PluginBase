package pluginbase.bukkit;

import pluginbase.command.CommandHandler;
import pluginbase.command.CommandRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BukkitCommandHandler extends CommandHandler<BukkitPlugin> {

    private final CommandExecutor executor;
    private CommandMap fallbackCommands;

    public BukkitCommandHandler(BukkitPlugin plugin) {
        this(plugin, plugin);
    }

    public BukkitCommandHandler(BukkitPlugin plugin, CommandExecutor executor) {
        super(plugin);
        this.executor = executor;
    }

    protected boolean register(@NotNull final CommandRegistration<BukkitPlugin> commandInfo, @NotNull final pluginbase.command.Command<BukkitPlugin> command) {
        CommandMap commandMap = getCommandMap();
        if (commandMap == null) {
            return false;
        }
        String[] aliases = commandInfo.getAliases();
        //String[] bukkitCompatAliases = new String[aliases.length];
        //for (int i = 0; i < aliases.length; i++) {
        //    bukkitCompatAliases[i] = aliases[i].split(" ")[0];
        //}
        DynamicPluginCommand cmd = new DynamicPluginCommand(aliases, commandInfo.getDesc(),
                "/" + commandInfo.getName() + " " + commandInfo.getUsage(), executor, commandInfo.getRegisteredWith(), plugin);
        CommandHelpTopic helpTopic = new CommandHelpTopic(cmd, command.getHelp());
        cmd.setPermissions(commandInfo.getPermissions());
        if (commandMap.register(commandInfo.getName(), plugin.getDescription().getName(), cmd)) {
            Bukkit.getServer().getHelpMap().addTopic(helpTopic);
            return true;
        }
        return false;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = ReflectionUtil.getField(plugin.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            if (fallbackCommands != null) {
                commandMap = fallbackCommands;
            } else {
                getLog().warning("Could not retrieve server CommandMap, using fallback instead!");
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
