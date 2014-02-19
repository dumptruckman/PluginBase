package pluginbase.command;

import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.Messaging;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.Perm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Commands for PluginBase's command handler must implement this class AND annotate it with the
 * {@link CommandInfo} annotation.
 *
 * @param <P> Typically represents a plugin implementing this command.
 */
public abstract class Command<P extends CommandProvider & Messaging> {

    private P plugin;

    /**
     * Constructs a command.
     * <p/>
     * You will never need to call this constructor.  It is used by {@link CommandHandler}
     *
     * @param plugin your plugin.
     */
    protected Command(@NotNull final P plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the plugin implementing this command.
     *
     * @return the plugin implementing this command.
     */
    @NotNull
    protected P getPluginBase() {
        return plugin;
    }

    /**
     * Gets the messager from the plugin implementing this command.
     *
     * @return the messager from the plugin implementing this command.
     */
    @NotNull
    protected Messager getMessager() {
        return getPluginBase().getMessager();
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
     * <p/>
     * If any parameter limitations are set in the {@link CommandInfo} then this method will only be called if the
     * executor used the correct amount of parameters.
     *
     * @param sender the person executing the command.
     * @param context contextual information about the execution of this command.
     * @return true if the command executed as expected or false if the user should be informed about correct usage
     * of this command.
     */
    public abstract boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context);
}
