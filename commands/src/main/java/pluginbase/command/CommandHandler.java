package pluginbase.command;

import pluginbase.logging.LogProvider;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.Messaging;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This class is responsible for handling commands.
 * <p/>
 * This entails everything from registering them to detecting executed commands and delegating them
 * to the appropriate command class.
 * <p/>
 * This must be implemented fully for a specific Minecraft server implementation.
 *
 * @param <P> Typically represents a plugin implementing this command handler.
 */
public abstract class CommandHandler<P extends CommandProvider & Messaging> {

    protected static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    @NotNull
    protected final P plugin;
    @NotNull
    protected final Map<String, Class<? extends Command>> registeredCommandClasses;
    private final CommandTree commandTree = new CommandTree();
    @NotNull
    private final Map<BasePlayer, QueuedCommand> queuedCommands = new HashMap<BasePlayer, QueuedCommand>();
    @NotNull
    private Map<CommandInfo, String> usageMap = new HashMap<CommandInfo, String>();

    /**
     * Creates a new command handler.
     * <p/>
     * Typically you only want one of these per plugin.
     *
     * @param plugin The plugin utilizing this command handler.
     */
    protected CommandHandler(@NotNull final P plugin) {
        this.plugin = plugin;
        this.registeredCommandClasses = new HashMap<String, Class<? extends Command>>();
        Messages.registerMessages(plugin, CommandHandler.class);
    }

    /**
     * Retrieves a PluginLogger for this CommandHandler which is inherited from the plugin passed in during construction.
     *
     * @return a PluginLogger for this CommandHandler.
     */
    @NotNull
    protected PluginLogger getLog() {
        return plugin.getLog();
    }

    //public boolean registerCommmands(String packageName) {

    //}

    /**
     * Registers the command represented by the given command class.
     *
     * @param commandClass the command class to register.
     * @return true if command registered successfully.
     * @throws IllegalArgumentException if there was some problem with the command class passed in.
     */
    public boolean registerCommand(@NotNull Class<? extends Command> commandClass) throws IllegalArgumentException {
        CommandBuilder<P> commandBuilder = new CommandBuilder<P>(plugin, commandClass);
        CommandRegistration<P> commandRegistration = commandBuilder.createCommandRegistration();
        assertNotAlreadyRegistered(commandClass, commandRegistration);
        Command command = commandBuilder.getCommand();
        if (register(commandRegistration, command)) {
            registerRootCommands(commandRegistration);
            cacheUsageString(commandBuilder);
            String[] aliases = commandRegistration.getAliases();
            for (String alias : aliases) {
                configureCommandKeys(alias);
                registeredCommandClasses.put(alias, commandClass);
            }
            // Register language in the command class if any.
            Messages.registerMessages(plugin, commandClass);
            getLog().fine("Registered command '%s' to: %s", aliases[0], commandClass);
            return true;
        }

        getLog().severe("Failed to register: " + commandClass);
        return false;
    }

    private static final String SUB_COMMAND_HELP = "Displays a list of sub-commands.";

    private void registerRootCommands(CommandRegistration<P> commandRegistration) {
        String[] aliases = commandRegistration.getAliases();
        Command<P> command = new DirectoryCommand<P>(plugin);
        for (String alias : aliases) {
            String[] args = PATTERN_ON_SPACE.split(alias);
            if (args.length > 1) {
                List<String> directoryAliases = new ArrayList<String>(args.length - 1);
                StringBuilder directoryAliasBuilder = new StringBuilder();
                for (int i = 0; i < args.length - 1; i++) {
                    if (i != 0) {
                        directoryAliasBuilder.append(" ");
                    }
                    directoryAliasBuilder.append(args[i]);
                    String directoryAlias = directoryAliasBuilder.toString();
                    if (!registeredCommandClasses.containsKey(directoryAlias)) {
                        directoryAliases.add(directoryAlias);
                        registeredCommandClasses.put(directoryAlias, DirectoryCommand.class);
                        configureCommandKeys(directoryAlias);
                        getLog().finer("Registered directory command '%s'", directoryAlias);
                    }
                }
                if (!directoryAliases.isEmpty()) {
                    CommandRegistration <P> directoryCommandRegistration = new CommandRegistration<P>(SUB_COMMAND_HELP, SUB_COMMAND_HELP,
                            directoryAliases.toArray(new String[directoryAliases.size()]), plugin);
                    register(directoryCommandRegistration, command);
                }
            }
        }
    }

    private void assertNotAlreadyRegistered(Class commandClass, CommandRegistration commandRegistration) {
        String[] aliases = commandRegistration.getAliases();
        for (String alias : aliases) {
            if (registeredCommandClasses.containsKey(alias)) {
                throw new IllegalArgumentException("The alias '" + alias + "' for '" + commandClass + "' has already been registered by '" + registeredCommandClasses.get(alias) + "'");
            }
        }
    }

    void configureCommandKeys(String primaryAlias) {
        commandTree.registerKeysForAlias(primaryAlias);
    }

    /**
     * Tells the server implementation to register the given command information as a command so that
     * someone using the command will delegate the execution to this plugin/command handler.
     *
     * @param commandInfo the info for the command to register.
     * @return true if successfully registered.
     */
    protected abstract boolean register(@NotNull final CommandRegistration<P> commandInfo, @NotNull final Command<P> command);



    void removedQueuedCommand(@NotNull final BasePlayer player, @NotNull final QueuedCommand command) {
        if (queuedCommands.containsKey(player) && queuedCommands.get(player).equals(command)) {
            queuedCommands.remove(player);
        }
    }

    /** Message used when a users tries to confirm a command but has not queued one or the queued one has expired. */
    public static final Message NO_QUEUED_COMMANDS = Message.createMessage("commands.queued.none_queued",
            Theme.SORRY + "Sorry, but you have not used any commands that require confirmation.");
    /** Default message used when the user must confirm a queued command. */
    public static final Message MUST_CONFIRM = Message.createMessage("commands.queued.must_confirm",
            Theme.DO_THIS + "You must confirm the previous command by typing " + Theme.CMD_HIGHLIGHT + "%s"
                    + "\n" + Theme.INFO + "You have %s to comply.");

    public static final Message PERMISSION_DENIED = Message.createMessage("commands.permission-denied",
            Theme.SORRY + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");

    /**
     * Confirms any queued command for the given player.
     *
     * @param player the player to confirm queued commands for.
     * @return true if there was a queued command.
     */
    public boolean confirmCommand(@NotNull final BasePlayer player) {
        final QueuedCommand queuedCommand = queuedCommands.get(player);
        if (queuedCommand != null) {
            queuedCommand.confirm();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Locates and runs a command executed by a user.
     *
     * @param player the user executing the command.
     * @param args the space separated arguments of the command including the base command itself.
     * @return true if the command executed successfully.
     * @throws SendablePluginBaseException if there were any exceptions brought about by the usage of the command.
     * <p/>
     * The causes are many fold and include things such as using an improper amount of parameters or attempting to
     * use a flag not recognized by the command.
     * TODO This needs to throw an extended PluginBaseException
     */
    public boolean locateAndRunCommand(@NotNull final BasePlayer player, @NotNull String[] args) throws CommandException {
        args = commandDetection(args);
        getLog().finest("'%s' is attempting to use command '%s'", player, Arrays.toString(args));
        if (this.plugin.useQueuedCommands()
                && !this.registeredCommandClasses.containsKey(this.plugin.getCommandPrefix() + "confirm")
                && args.length == 2
                && args[0].equalsIgnoreCase(this.plugin.getCommandPrefix())
                && args[1].equalsIgnoreCase("confirm")) {
            getLog().finer("No confirm command registered, using built in confirm...");
            if (!confirmCommand(player)) {
                this.plugin.getMessager().message(player, NO_QUEUED_COMMANDS);
            }
            return true;
        }
        final Class<? extends Command> commandClass = registeredCommandClasses.get(args[0]);
        if (commandClass == null) {
            getLog().severe("Could not locate registered command '" + args[0] + "'");
            return false;
        }
        final Command command = CommandLoader.loadCommand(plugin, commandClass);
        if (command instanceof DirectoryCommand) {
            ((DirectoryCommand) command).runCommand(player, args[0], commandTree.getTreeAt(args[0]));
            return true;
        }
        if (command.getPerm() != null && !command.getPerm().hasPermission(player)) {
            BundledMessage permissionMessage = command.getPermissionMessage();
            if (permissionMessage == null) {
                permissionMessage = Message.bundleMessage(PERMISSION_DENIED);
            }
            plugin.getMessager().message(player, permissionMessage);
            return false;
        }
        final CommandInfo cmdInfo = command.getClass().getAnnotation(CommandInfo.class);
        if (cmdInfo == null) {
            getLog().severe("Missing CommandInfo for command: " + args[0]);
            return false;
        }
        final Set<Character> valueFlags = new HashSet<Character>();

        char[] flags = cmdInfo.flags().toCharArray();
        final Set<Character> newFlags = new HashSet<Character>();
        for (int i = 0; i < flags.length; ++i) {
            if (flags.length > i + 1 && flags[i + 1] == ':') {
                valueFlags.add(flags[i]);
                ++i;
            }
            newFlags.add(flags[i]);
        }
        final CommandContext context = new CommandContext(args, valueFlags);
        if (context.argsLength() < cmdInfo.min()) {
            throw new CommandUsageException(Message.bundleMessage(TOO_FEW_ARGUMENTS), getUsage(args, 0, command, cmdInfo));
        }
        if (cmdInfo.max() != -1 && context.argsLength() > cmdInfo.max()) {
            throw new CommandUsageException(Message.bundleMessage(TOO_MANY_ARGUMENTS), getUsage(args, 0, command, cmdInfo));
        }
        if (!cmdInfo.anyFlags()) {
            for (char flag : context.getFlags()) {
                if (!newFlags.contains(flag)) {
                    throw new CommandUsageException(Message.bundleMessage(UNKNOWN_FLAG, flag), getUsage(args, 0, command, cmdInfo));
                }
            }
        }
        if (!command.runCommand(player, context)) {
            throw new CommandUsageException(Message.bundleMessage(USAGE_ERROR), getUsage(args, 0, command, cmdInfo));
        }
        if (command instanceof QueuedCommand) {
            final QueuedCommand queuedCommand = (QueuedCommand) command;
            getLog().finer("Queueing command '%s' for '%s'", queuedCommand, player);
            queuedCommands.put(player, queuedCommand);
            final BundledMessage confirmMessage = queuedCommand.getConfirmMessage();
            this.plugin.getMessager().message(player, confirmMessage);
        }
        return true;
    }

    public String[] commandDetection(@NotNull final String[] split) {
        return commandTree.joinArgsForKnownCommands(split);
    }

    public static final Message TOO_FEW_ARGUMENTS = Message.createMessage("commands.usage.too_few_arguments",
            Theme.ERROR + "Too few arguments.");
    public static final Message TOO_MANY_ARGUMENTS = Message.createMessage("commands.usage.too_many_arguments",
            Theme.ERROR + "Too many arguments.");
    public static final Message UNKNOWN_FLAG = Message.createMessage("commands.usage.unknown_flag", Theme.ERROR + "Unknown flag: " + Theme.VALUE + "%s");
    public static final Message USAGE_ERROR = Message.createMessage("commands.usage.usage_error", Theme.ERROR + "Usage error...");

    public static final Message VALUE_FLAG_ALREADY_GIVEN = Message.createMessage("commands.usage.value_flag_already_given",
            Theme.ERROR + "Value flag '" + Theme.VALUE + "%s" + Theme.ERROR + "' already given");
    public static final Message NO_VALUE_FOR_VALUE_FLAG = Message.createMessage("commands.usage.must_specify_value_for_value_flag",
            Theme.ERROR + "No value specified for the '" + Theme.VALUE + "-%s" + Theme.ERROR + "' flag.");

    public static final Message SUB_COMMAND_LIST = Message.createMessage("commands.sub_command_list",
            Theme.INFO + "The following is a list of sub-commands for '" + Theme.VALUE + "%s" + Theme.INFO + "':\n%s");

    /**
     * Returns a list of strings detailing the usage of the given command.
     *
     * @param args
     * @param level
     * @param cmd
     * @param cmdInfo
     * @return
     */
    protected List<String> getUsage(@NotNull final String[] args, final int level, final Command cmd, @NotNull final CommandInfo cmdInfo) {
        final List<String> commandUsage = new ArrayList<String>();
        final StringBuilder command = new StringBuilder();
        command.append(Theme.CMD_USAGE);
        command.append('/');
        for (int i = 0; i <= level; ++i) {
            command.append(args[i]);
            command.append(' ');
        }
        command.append(getArguments(cmdInfo));
        commandUsage.add(command.toString());

        final String help;
        final Message helpMessage = cmd.getHelp();
        if (helpMessage != null) {
            help = plugin.getMessager().getLocalizedMessage(helpMessage);
        } else {
            help = "";
        }
        if (!help.isEmpty()) {
            commandUsage.add(help);
        }

        return commandUsage;
    }

    private void cacheUsageString(CommandBuilder commandBuilder) {
        usageMap.put(commandBuilder.getCommandInfo(), commandBuilder.getCommandUsageString());
    }

    protected String getArguments(@NotNull final CommandInfo cmdInfo) {
        return usageMap.containsKey(cmdInfo) ? usageMap.get(cmdInfo) : "";
    }

    protected static class CommandRegistration<P extends CommandProvider & Messaging> {

        private final String[] aliases;
        private final P registeredWith;
        private final String usage, desc;
        private final String[] permissions;

        CommandRegistration(String usage, String desc, String[] aliases, P registeredWith) {
            this(usage, desc, aliases, registeredWith, null);
        }

        CommandRegistration(String usage, String desc, String[] aliases, P registeredWith, String[] permissions) {
            this.usage = usage;
            this.desc = desc;
            this.aliases = aliases;
            this.permissions = permissions;
            this.registeredWith = registeredWith;
        }

        public String[] getAliases() {
            return aliases;
        }

        public String getName() {
            return aliases[0];
        }

        public String getUsage() {
            return usage;
        }

        public String getDesc() {
            return desc;
        }

        public String[] getPermissions() {
            return permissions;
        }

        public P getRegisteredWith() {
            return registeredWith;
        }
    }
}
