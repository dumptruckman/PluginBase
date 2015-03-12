package pluginbase.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import pluginbase.bukkit.minecraft.BukkitTools;
import pluginbase.command.CommandException;
import pluginbase.command.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.CommandProvider;
import pluginbase.command.CommandUsageException;
import pluginbase.minecraft.BasePlayer;

import java.util.Collections;
import java.util.List;


/**
 * A Bukkit implementation of {@link CommandHandler}.
 */
public final class BukkitCommandHandler extends CommandHandler implements TabExecutor {

    private final Plugin plugin;
    private CommandMap fallbackCommands;

    /**
     * Creates a new instance of the bukkit command handler.  You'll only need one of these per plugin.
     *
     * @param commandProvider Probably the plugin implementing commands.  If you are using the Plugin/Plugin-Bukkit module this
     *               should be your PluginBase instance and this will be created for you.  Otherwise, it will probably
     *               be your plugin main class instance.
     * @param plugin Your plugin main class instance.
     */
    public BukkitCommandHandler(@NotNull CommandProvider commandProvider, @NotNull Plugin plugin) {
        super(commandProvider);
        this.plugin = plugin;
    }

    protected boolean register(@NotNull final CommandRegistration commandInfo, @NotNull final pluginbase.command.Command command) {
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
        DynamicPluginCommand cmd = new DynamicPluginCommand(aliases, commandInfo.getDesc(),
                "/" + commandInfo.getName() + " " + commandInfo.getUsage(), this, commandInfo.getRegisteredWith(), plugin);
        CommandHelpTopic helpTopic = new CommandHelpTopic(cmd, command.getHelp());
        cmd.setPermissions(commandInfo.getPermissions());
        if (commandMap.register(commandInfo.getName(), commandProvider.getName(), cmd)) {
            Bukkit.getServer().getHelpMap().addTopic(helpTopic);
            /*
            PluginCommand pluginCommand = Bukkit.getServer().getPluginCommand(cmd.getName());
            if (pluginCommand == null || pluginCommand.getPlugin() != plugin) {
                pluginCommand = Bukkit.getServer().getPluginCommand(plugin.getName().toLowerCase() + ":" + cmd.getName());
            }
            if (pluginCommand == null) {
                throw new IllegalStateException("Cannot locate command " + plugin.getName().toLowerCase() + ":" + cmd.getName() + " on the Bukkit server.");
            }
            pluginCommand.setExecutor(this);
            */
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
        return BukkitTools.wrapSender(sender).hasPermission(permission);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!plugin.isEnabled()) {
            sender.sendMessage("This plugin is Disabled!");
            return true;
        }
        final BasePlayer wrappedSender = BukkitTools.wrapSender(sender);
        return callCommand(wrappedSender, command.getName(), args);
    }

    private boolean callCommand(@NotNull BasePlayer sender, @NotNull String commandName, @NotNull String[] args) {
        args = joinCommandWithArgs(commandName, args);
        try {
            return locateAndRunCommand(sender, args);
        } catch (CommandException e) {
            e.sendException(commandProvider.getMessager(), sender);
            if (e instanceof CommandUsageException) {
                for (final String usageString : ((CommandUsageException) e).getUsage()) {
                    sender.sendMessage(usageString);
                }
            }
        }
        return true;
    }

    private String[] joinCommandWithArgs(String commandName, String[] args) {
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = commandName;
        System.arraycopy(args, 0, allArgs, 1, args.length);
        return allArgs;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        args = joinCommandWithArgs(command.getName(), args);
        final BasePlayer wrappedSender = BukkitTools.wrapSender(sender);
        return tabComplete(wrappedSender, args);
    }
}
