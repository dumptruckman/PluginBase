package pluginbase.sponge.minecraft;

import com.flowpowered.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandSource;
import pluginbase.minecraft.BasePlayer;
import pluginbase.minecraft.location.Vector;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Contains useful utility methods for operations related to Bukkit.
 */
public class SpongeTools {

    private static Game GAME;
    private static final Map<CommandSource, BasePlayer> BASE_PLAYER_MAP = new WeakHashMap<>();

    private SpongeTools() {
        throw new AssertionError();
    }

    public static void registerGameInstance(Game game) {
        GAME = game;
    }

    public static Game getGame() {
        return GAME;
    }

    public static Server getServer() {
        return GAME.getServer();
    }

    /**
     * Wraps a sponge Player object into a BasePlayer object for use throughout PluginBase.
     *
     * @param player the Player to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
    public static BasePlayer wrapPlayer(@NotNull final Player player) {
        BasePlayer basePlayer = BASE_PLAYER_MAP.get(player);
        if (basePlayer == null) {
            basePlayer = new SpongePlayer(player);
            BASE_PLAYER_MAP.put(player, basePlayer);
        }
        return basePlayer;
    }

    /**
     * Wraps a sponge CommandSource object into a BasePlayer object for use throughout PluginBase.
     *
     * @param sender the CommandSender to wrap.
     * @return the wrapped BasePlayer.
     */
    @NotNull
    public static BasePlayer wrapSender(@NotNull final CommandSource sender) {
        if (sender instanceof Player) {
            return wrapPlayer((Player) sender);
        }
        BasePlayer basePlayer = BASE_PLAYER_MAP.get(sender);
        if (basePlayer == null) {
            basePlayer = new SpongeCommandSource(sender);
            BASE_PLAYER_MAP.put(sender, basePlayer);
        }
        return basePlayer;
    }

    /**
     * Converts {@link com.flowpowered.math.vector.Vector3d} objects to
     * {@link pluginbase.minecraft.location.Vector} objects.
     *
     * @param v The Sponge vector.
     * @return The PluginBase vector.
     */
    @NotNull
    public static Vector convertVector(@NotNull final Vector3d v) {
        return new Vector(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Converts {@link pluginbase.minecraft.location.Vector} objects
     * to {@link com.flowpowered.math.vector.Vector3d} objects.
     *
     * @param v The PluginBase vector.
     * @return The Sponge vector.
     */
    @NotNull
    public static Vector3d convertVector(final Vector v) {
        return new Vector3d(v.getX(), v.getY(), v.getZ());
    }
}
