package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.BuiltInCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.sk89q.bukkit.util.DynamicPluginCommand;
import com.sk89q.bukkit.util.DynamicPluginCommandHelpTopic;
import com.sk89q.bukkit.util.FallbackRegistrationListener;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BukkitCommandHandler implements CommandHandler {

    static {
        Bukkit.getServer().getHelpMap().registerHelpTopicFactory(DynamicPluginCommand.class, new DynamicPluginCommandHelpTopic.Factory());
    }

    private final BukkitPlugin plugin;
    private final CommandExecutor executor;
    private CommandMap fallbackCommands;

    private final Map<String, String> commandMap;

    public BukkitCommandHandler(BukkitPlugin plugin) {
        this(plugin, plugin);
    }

    public BukkitCommandHandler(BukkitPlugin plugin, CommandExecutor executor) {
        this.plugin = plugin;
        this.executor = executor;
        this.commandMap = new HashMap<String, String>();
    }

    @Override
    public boolean registerCommand(Class<? extends Command> commandClass) {
        final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
        if (cmdInfo == null) {
            throw new IllegalArgumentException("Command must be annotated with @CommandInfo");
        }
        final Command command = loadCommand(commandClass);
        if (command == null) {
            return false;
        }

        final String[] aliases;
        if (command instanceof BuiltInCommand) {
            aliases = new String[cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + ((BuiltInCommand) command).getStaticAliases().size()
                    + 1];
        } else {
            aliases = new String[cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + 1];
        }
        if (cmdInfo.directlyPrefixPrimary()) {
            aliases[0] = plugin.getCommandPrefix() + cmdInfo.primaryAlias();
        } else if (cmdInfo.prefixPrimary())  {
            aliases[0] = plugin.getCommandPrefix() + " " + cmdInfo.primaryAlias();
        } else {
            aliases[0] = cmdInfo.primaryAlias();
        }
        if (commandMap.containsKey(aliases[0])) {
            throw new IllegalArgumentException("Command with the same primary alias has already been registered!");
        }
        System.arraycopy(cmdInfo.aliases(), 0, aliases, 1, cmdInfo.aliases().length);
        int start = 1 + cmdInfo.aliases().length;
        for (int i = 0; i < cmdInfo.prefixedAliases().length; i++) {
            aliases[start + i] = plugin.getCommandPrefix() + " " + cmdInfo.prefixedAliases()[i];
        }
        start = 1 + cmdInfo.aliases().length + cmdInfo.prefixedAliases().length;
        for (int i = 0; i < cmdInfo.directlyPrefixedAliases().length; i++) {
            aliases[start + i] = plugin.getCommandPrefix() + " " + cmdInfo.directlyPrefixedAliases()[i];
        }
        if (command instanceof BuiltInCommand) {
            final BuiltInCommand builtInCommand = (BuiltInCommand) command;
            start = 1 + cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length;
            for (int i = 0; i < builtInCommand.getStaticAliases().size(); i++) {
                aliases[start + i] = builtInCommand.getStaticAliases().get(i);
            }
        }
        final String[] permissions;
        if (command.getPerm() != null) {
            permissions = new String[1];
            permissions[0] = command.getPerm().getName();
        } else {
            permissions = new String[0];
        }
        final com.sk89q.bukkit.util.CommandInfo bukkitCmdInfo = new com.sk89q.bukkit.util.CommandInfo(cmdInfo.usage(), cmdInfo.desc(), aliases, this, permissions);
        if (register(bukkitCmdInfo)) {
            commandMap.put(aliases[0], commandClass.getName());
            return true;
        }
        return false;
    }

    private Command loadCommand(final Class clazz) {
        try {
            return (Command) clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean register(com.sk89q.bukkit.util.CommandInfo command) {
        CommandMap commandMap = getCommandMap();
        if (command == null || commandMap == null) {
            return false;
        }
        DynamicPluginCommand cmd = new DynamicPluginCommand(command.getAliases(),
                command.getDesc(), "/" + command.getAliases()[0] + " " + command.getUsage(), executor, command.getRegisteredWith(), plugin);
        cmd.setPermissions(command.getPermissions());
        commandMap.register(plugin.getDescription().getName(), cmd);
        return true;
    }

    public CommandMap getCommandMap() {
        CommandMap commandMap = ReflectionUtil.getField(plugin.getServer().getPluginManager(), "commandMap");
        if (commandMap == null) {
            if (fallbackCommands != null) {
                commandMap = fallbackCommands;
            } else {
                Bukkit.getServer().getLogger().severe(plugin.getDescription().getName() +
                        ": Could not retrieve server CommandMap, using fallback instead! Please report to http://redmine.sk89q.com");
                fallbackCommands = commandMap = new SimpleCommandMap(Bukkit.getServer());
                Bukkit.getServer().getPluginManager().registerEvents(new FallbackRegistrationListener(fallbackCommands), plugin);
            }
        }
        return commandMap;
    }

    boolean hasPermission(final CommandSender sender, final String permission) {
        return plugin.wrapSender(sender).hasPermission(permission);
    }

    @Override
    public boolean locateAndRunCommand(BasePlayer player, String[] args) {
        final String className = commandMap.get(args[0]);
        if (className == null) {
            return false;
        }
        try {
            final Class<Command> commandClass = (Class<Command>) Class.forName(className);
            final Command command = loadCommand(commandClass);
            final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
            if (cmdInfo == null) {
                Logging.severe("Missing CommandInfo for command: " + args[0]);
                return false;
            }
            final Set<Character> flags = new HashSet<Character>();
            for (char flag : cmdInfo.flags().toCharArray()) {
                flags.add(flag);
            }
            try {
                command.runCommand(player, plugin, new CommandContext(args, flags));
                return true;
            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
