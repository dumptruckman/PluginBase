package pluginbase.command;

import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.messaging.Messager;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * A PluginBase user command.
 * <br>
 * This is for commands to be used on the server by the server operator or the players on the server.
 * <br/>
 * Commands for PluginBase's command handler <b>must</b> implement this class AND annotate it with the
 * {@link CommandInfo} annotation.
 *
 * @param <P> the plugin that this command belongs to.
 */
public abstract class Command<P> {

    private final CommandProvider<P> commandProvider;

    /**
     * Constructs a command.
     * <br>
     * You will never need to call this constructor.  It is used by {@link CommandHandler}.
     *
     * @param commandProvider the command's provider.
     */
    protected Command(@NotNull final CommandProvider<P> commandProvider) {
        this.commandProvider = commandProvider;
    }

    /**
     * Gets the provider of this command.
     *
     * @return the provider of this command.
     */
    @NotNull
    protected CommandProvider<P> getCommandProvider() {
        return commandProvider;
    }

    /**
     * Gets the owner of this command.
     *
     * @return the command owner.
     */
    protected P getPlugin() {
        return getCommandProvider().getPlugin();
    }

    /**
     * Gets the messager from the provider of this command.
     *
     * @return the messager from the provider of this command.
     */
    @NotNull
    protected Messager getMessager() {
        return getCommandProvider().getMessager();
    }

    /**
     * Gets the permission required to use this command.
     *
     * @return the permission required to use this command or null if no permission is required.
     */
    @Nullable
    public abstract Perm getPerm();

    /**
     * Gets the permission denied message for this command.
     *
     * @return the permission denied message for this command or null if the default message should be used.
     */
    @Nullable
    public BundledMessage getPermissionMessage() {
        return null;
    }

    /**
     * Gets the help message for this command.
     *
     * @return the help message for this command or null if none available.
     */
    @Nullable
    public abstract Message getHelp();

    /**
     * This is the method called when someone executes this command.
     * <br>
     * If any parameter limitations are set in the {@link CommandInfo} then this method will only be called if the
     * executor used the correct amount of parameters.
     *
     * @param sender the person executing the command.
     * @param context contextual information about the execution of this command.
     * @return true if the command executed as expected or false if the user should be informed about correct usage
     * of this command.
     * @throws CommandException uncaught command exceptions thrown by this method will be message the user with
     * the message in {@link pluginbase.command.CommandException#getBundledMessage()}.  If the exception is an instance
     * of {@link pluginbase.command.CommandUsageException} the command sender will also be messaged the usage for the
     * command as listed in this command's {@link pluginbase.command.CommandInfo} annotation.
     */
    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) throws CommandException;

    public List<String> tabComplete(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        return Collections.emptyList();
    }
}
