package pluginbase.bukkit.minecraft;

import pluginbase.minecraft.BasePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract implementation of BasePlayer for Bukkit.
 * <p/>
 * This will be implemented separately for each CommandSender type as required.
 *
 * @param <S> The implementation of CommandSender used.  Should be implemented as non-generic in the concrete
 *           implementation of AbstractBukkitCommandSender.
 */
abstract class AbstractBukkitCommandSender<S extends CommandSender> extends BasePlayer {

    @NotNull
    private final S sender;

    AbstractBukkitCommandSender(@NotNull final S sender) {
        this.sender = sender;
    }

    /**
     * Gets the sender represented by this BasePlayer.
     *
     * @return the sender represented by this BasePlayer.
     */
    @NotNull
    protected S getSender() {
        return this.sender;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return getSender().getName();
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPermission(@NotNull final String perm) {
        return getSender().hasPermission(perm);
    }

    /** {@inheritDoc} */
    @Override
    public void sendMessage(@NotNull final String message) {
        getSender().sendMessage(message);
    }
}
