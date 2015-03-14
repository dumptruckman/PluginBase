package pluginbase.sponge.minecraft;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.util.command.CommandSource;
import pluginbase.minecraft.BasePlayer;

/**
 * An abstract implementation of BasePlayer for Sponge.
 * <p/>
 * This will be implemented separately for each CommandSource type as required.
 *
 * @param <S> The implementation of CommandSource used.  Should be implemented as non-generic in the concrete
 *           implementation of AbstractSpongeCommandSource.
 */
public class AbstractSpongeCommandSource<S extends CommandSource> extends BasePlayer {

    @NotNull
    private final S source;

    /**
     * Gets the source represented by this BasePlayer.
     *
     * @return the source represented by this BasePlayer.
     */
    @NotNull
    protected S getSender() {
        return this.source;
    }

    AbstractSpongeCommandSource(@NotNull final S source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public boolean hasPermission(@NotNull String perm) {
        return source.hasPermission(perm);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        source.sendMessage(message);
    }
}
